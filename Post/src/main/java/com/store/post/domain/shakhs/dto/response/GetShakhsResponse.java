package com.store.post.domain.shakhs.dto.response;

import com.store.post.domain.shakhs.entity.Shakhs;

import java.util.List;

public class GetShakhsResponse {

    List<Shakhs> shakhs;
    int count;

    public List<Shakhs> getShakhs() {
        return shakhs;
    }

    public void setShakhs(List<Shakhs> shakhs) {
        this.shakhs = shakhs;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public GetShakhsResponse(List<Shakhs> shakhs, int count) {
        this.shakhs = shakhs;
        this.count = count;
    }
}
