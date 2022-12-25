package com.xapaya.xapaporra.porra.repository;

import com.xapaya.xapaporra.porra.dto.CurrentSeasonDto;

public interface CurrentSeasonRepositoryCustom {
    void upsert(CurrentSeasonDto dto);
}
