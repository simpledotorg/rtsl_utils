package org.rtsl.config.dynamic.folder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.rtsl.config.dynamic.DynamicConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderCrawler<KEY, TARGET> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCrawler.class);

    private final DynamicConfigRegistry<String, KEY, TARGET> metaFactory;
    private final File folder;

    public FolderCrawler(DynamicConfigRegistry<String, KEY, TARGET> metaFactory, File folder) {
        this.metaFactory = metaFactory;
        this.folder = folder;
        if (!folder.isDirectory()) {
            throw new InvalidParameterException("Not a directory: " + folder.getAbsolutePath());
        }

    }

    public Map<KEY, TARGET> getAll() throws IOException {
        LOGGER.info("Parsing all files from folder <{}>", folder.getAbsolutePath());
        Map<KEY, TARGET> returnMap = new HashMap<>();
        Collection<File> files = FileUtils.listFiles(folder, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY);
        for (File currentFile : files) { // TODO: logzzz
            String currentFileName = currentFile.getName();
            LOGGER.debug("Parsing file <{}>", currentFileName);
            String currentFileContent = readFile(currentFile.getAbsolutePath(), Charset.defaultCharset());
            // TODO : optionnally modify the string to add the file name ?
            // rather dirty, but could work
            KEY currentKey = metaFactory.getKey(currentFileContent);
            if (currentKey instanceof FileNameAware currentFileNameAware) {
                currentFileNameAware.setFileName(currentFileName);
            }
            TARGET currentObject = metaFactory.apply(currentFileContent);
            if (currentObject != null) {
                LOGGER.info("Finshed parsing file <{}>. Resulting key is <{}>, resulting object is <{}>", currentFileName, currentKey, currentObject);
                returnMap.put(currentKey, currentObject);
            } else {
                LOGGER.warn("Obtained <null> object for file <{}>. Resulting key is <{}>", currentFileName, currentKey);
            }
        }
        return returnMap;
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
