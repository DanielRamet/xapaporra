package com.xapaya.xapaporra.repository;

import com.xapaya.xapaporra.dto.CurrentSeasonDto;

public interface CurrentSeasonRepositoryCustom {
    void upsert(CurrentSeasonDto dto);
}
