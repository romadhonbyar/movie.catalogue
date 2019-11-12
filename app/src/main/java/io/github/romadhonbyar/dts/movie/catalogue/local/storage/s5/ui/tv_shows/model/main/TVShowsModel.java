package io.github.romadhonbyar.dts.movie.catalogue.local.storage.s5.ui.tv_shows.model.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TVShowsModel {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<TVShowsModelResults> results = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<TVShowsModelResults> getResults() {
        return results;
    }

    public void setResults(List<TVShowsModelResults> results) {
        this.results = results;
    }
}