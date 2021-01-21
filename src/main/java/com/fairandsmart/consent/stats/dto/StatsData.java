package com.fairandsmart.consent.stats.dto;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
