package com.eleks.academy.pharmagator;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.springframework.util.ResourceUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractDataIT {

    protected IDataSet readDataset(@NotNull String datasetFileName) throws DataSetException, IOException {

        String resourceLocationTemplate = "src/test/resources/%s";

        String resourceLocation = String.format(resourceLocationTemplate, datasetFileName);

        File file = ResourceUtils.getFile(resourceLocation);

        try (var resource = new FileInputStream(file)) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }
}
