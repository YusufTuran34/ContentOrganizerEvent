package com.contentorganizer.youtube.service;

import com.contentorganizer.youtube.config.FFmpegConfig;
import com.contentorganizer.youtube.config.YouTubeConfig;
import com.contentorganizer.youtube.dto.VideoStreamRequest;
import com.contentorganizer.youtube.model.VideoMetadata;
import com.contentorganizer.youtube.model.VideoQuality;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FFmpegService {
    
    private static final Logger logger = LoggerFactory.getLogger(FFmpegService.class);
    
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;
    private final FFmpegConfig ffmpegConfig;
    private final YouTubeConfig youTubeConfig;
    
    public void init() throws IOException {
        // This method is now empty as the FFmpeg and FFprobe objects are injected
    }

    public void startStreaming(VideoStreamRequest request) throws IOException {
        validateVideoFile(request.getInputPath());

        VideoQuality quality = youTubeConfig.getStream().getQuality();
        
        FFmpegBuilder builder = new FFmpegBuilder()
            .setInput(request.getInputPath())
            .overrideOutputFiles(true)
            .addOutput(youTubeConfig.getRtmpUrl() + "/" + youTubeConfig.getStreamKey())
            .setFormat("flv")
            .setVideoCodec("libx264")
            .setVideoFrameRate(youTubeConfig.getStream().getFrameRate())
            .setVideoResolution(quality.getWidth(), quality.getHeight())
            .setVideoBitRate(quality.getBitrate())
            .setAudioCodec("aac")
            .setAudioBitRate(128000)
            .setAudioChannels(2)
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
            .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

    public VideoMetadata getVideoMetadata(String videoPath) throws IOException {
        validateVideoFile(videoPath);
        FFmpegProbeResult probeResult = ffprobe.probe(videoPath);
        FFmpegStream stream = probeResult.getStreams().get(0);

        return VideoMetadata.builder()
            .durationSeconds((long) probeResult.getFormat().duration)
            .fileSizeBytes(probeResult.getFormat().size)
            .width(stream.width)
            .height(stream.height)
            .build();
    }

    private void validateVideoFile(String videoPath) throws IOException {
        Path path = Paths.get(videoPath);
        if (!Files.exists(path)) {
            throw new IOException("Video file does not exist: " + videoPath);
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("Path is not a regular file: " + videoPath);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("Video file is not readable: " + videoPath);
        }
    }

    private String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }

    private boolean isValidVideoExtension(String extension) {
        return extension.matches("^(mp4|avi|mov|wmv|flv|mkv|webm)$");
    }
    
    public CompletableFuture<Void> streamToYouTube(VideoStreamRequest request) {
        return CompletableFuture.runAsync(() -> {
            try {
                validateVideoFile(request.getInputPath());
                
                String rtmpUrl = youTubeConfig.getRtmpUrl();
                String streamKey = youTubeConfig.getStream().getKey();
                String streamUrl = rtmpUrl + "/" + streamKey;
                
                // Set video resolution based on quality setting
                VideoQuality quality = youTubeConfig.getStream().getQuality();
                int width, height;
                switch (quality) {
                    case HIGH:
                        width = 1920;
                        height = 1080;
                        break;
                    case MEDIUM:
                        width = 1280;
                        height = 720;
                        break;
                    default: // LOW
                        width = 854;
                        height = 480;
                        break;
                }

                FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(request.getInputPath())
                    .overrideOutputFiles(true)
                    .addOutput(streamUrl)
                    .setFormat("flv")
                    .setAudioCodec("aac")
                    .setAudioBitRate(128000)
                    .setVideoCodec("libx264")
                    .setVideoFrameRate(youTubeConfig.getStream().getFrameRate())
                    .setVideoBitRate(quality.getBitrate() * 1000)
                    .setVideoWidth(width)
                    .setVideoHeight(height)
                    .addExtraArgs("-maxrate", quality.getBitrate() + "k")
                    .addExtraArgs("-bufsize", (quality.getBitrate() * 2) + "k")
                    .done();

                FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
                executor.createJob(builder).run();
                
                log.info("Streaming completed successfully");
            } catch (IOException e) {
                log.error("Streaming failed", e);
                throw new RuntimeException("Failed to stream video: " + e.getMessage(), e);
            }
        });
    }
    
    public CompletableFuture<String> prepareVideoForUpload(String inputPath, String outputDir) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (ffmpeg == null || ffprobe == null) {
                    throw new RuntimeException("FFmpeg or FFprobe not initialized");
                }
                
                validateVideoFile(inputPath);
                
                // Create output directory if it doesn't exist
                Path outputDirPath = Paths.get(outputDir);
                Files.createDirectories(outputDirPath);
                
                // Generate output filename
                String fileName = Paths.get(inputPath).getFileName().toString();
                String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
                String outputPath = outputDir + "/" + nameWithoutExt + "_processed.mp4";
                
                // Prepare video for YouTube upload (ensure compatibility)
                FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputPath)
                    .overrideOutputFiles(true)
                    .addOutput(outputPath)
                    .setFormat("mp4")
                    .setVideoCodec("libx264")
                    .setAudioCodec("aac")
                    .setVideoFrameRate(30)
                    .setAudioSampleRate(44100)
                    .addExtraArgs("-preset", "medium")
                    .addExtraArgs("-crf", "23")
                    .addExtraArgs("-pix_fmt", "yuv420p")
                    .addExtraArgs("-movflags", "+faststart")
                    .done();
                
                FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
                
                log.info("Processing video for upload: {} -> {}", inputPath, outputPath);
                executor.createJob(builder).run();
                
                log.info("Video processing completed: {}", outputPath);
                return outputPath;
                
            } catch (Exception e) {
                log.error("Error processing video for upload: {}", inputPath, e);
                throw new RuntimeException("Failed to process video: " + e.getMessage(), e);
            }
        });
    }
} 