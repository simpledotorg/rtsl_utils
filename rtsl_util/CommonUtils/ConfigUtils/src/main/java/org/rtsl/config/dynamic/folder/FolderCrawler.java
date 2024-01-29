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

public class FolderCrawler<K> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderCrawler.class);

    private final DynamicConfigRegistry<String, K> metaFactory;
    private final File folder;

    public FolderCrawler(DynamicConfigRegistry metaFactory, File folder) {
        this.metaFactory = metaFactory;
        this.folder = folder;
        if (!folder.isDirectory()) {
            throw new InvalidParameterException("Not a directory: " + folder.getAbsolutePath());
        }

    }

    public Map<String, K> getAll() throws IOException {
        LOGGER.info("Parsing all files from folder <{}>", folder.getAbsolutePath());
        Map<String, K> returnMap = new HashMap<>();
        Collection<File> files = FileUtils.listFiles(folder, new RegexFileFilter("^(.*?)"), DirectoryFileFilter.DIRECTORY
        );
        for (File currentFile : files) { // TODO: logzzz
            String currentKey = currentFile.getName();
            LOGGER.info("Parsing file <{}>", currentKey);
            String currentString = readFile(currentFile.getAbsolutePath(), Charset.defaultCharset());
            K currentObject = metaFactory.apply(currentString);
            returnMap.put(currentKey, currentObject);
        }
        return returnMap;
    }

    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
