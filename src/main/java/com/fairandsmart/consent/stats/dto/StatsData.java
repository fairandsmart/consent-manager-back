package com.fairandsmart.consent.stats.dto;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(name="StatsData", description="Map that holds all StatsCharts data.")
public class StatsData {

    private List<StatsDataSet> datasets;
    private List<String> labels;

    public StatsData() {
        datasets = new ArrayList<>();
        labels = new ArrayList<>();
    }

    public List<StatsDataSet> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<StatsDataSet> datasets) {
        this.datasets = datasets;
    }

    public void addDataset(StatsDataSet dataset) {
        this.datasets.add(dataset);
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public void addLabel(String label) {
        this.labels.add(label);
    }

    @Override
    public String toString() {
        return "StatsData{" +
                "datasets=" + datasets +
                ", labels=" + labels +
                '}';
    }

}
