package com.jayfella.sdk.core.tasks;

import com.google.common.io.Files;
import com.jayfella.sdk.core.background.BackgroundTask;
import com.jayfella.sdk.project.Project;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class CompileProjectBackgroundTask extends BackgroundTask {

    private static final Logger log = LoggerFactory.getLogger(CompileProjectBackgroundTask.class);

    public CompileProjectBackgroundTask() {
        super("Compile Project");
    }

    @Override
    public void execute() {

        String projectPath = Project.getOpenProject().getProjectPath();

        if (SystemUtils.IS_OS_LINUX) {

            log.info("Compiling project.");
            setStatus("Compiling project...");

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(projectPath + "//"));
            processBuilder.command("bash", "-c", "./gradlew shadowJar");

            try {

                Process process = processBuilder.start();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    log.info(line);
                    setStatus(line);
                }

                int exitCode = process.waitFor();

                if (exitCode == 0) {
                    log.info("Compile completed successfully.");
                    setStatus("Compile completed successfully.");
                }
                else {
                    log.error("Exited with error code: " + exitCode);
                    setStatus("Exited with error code: " + exitCode);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            log.info("Operating System not implemented yet.");
        }

        // copy the compiled lib to "./dist" as "dist.jar

        File distFolder = new File(projectPath, "./dist");
        File[] existingFiles = distFolder.listFiles();

        if (existingFiles != null) {
            for (File file : existingFiles) {
                file.delete();
                log.info("Deleting old jar.");
                setStatus("Deleting old Jar...");
            }
        }

        // ./build/libs/*-all.jar

        File buildFolder = new File(projectPath, "/build/libs/");
        File[] buildFiles = buildFolder.listFiles();

        File allJar = Arrays.stream(buildFiles)
                .filter(file -> file.getName().endsWith("-all.jar"))
                .findFirst()
                .orElse(null);

        if (allJar != null && allJar.exists()) {

            log.info("Copying dist jar.");
            setStatus("Copying dist jar...");

            try {
                Files.copy(allJar, new File(projectPath, "/dist/dist.jar"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log.info("Compile completed.");
        setStatus("Compile completed.");

    }

}
