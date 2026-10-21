package com.example.spring_rag.controller;

import com.example.spring_rag.dto.ApiResponse;
import com.example.spring_rag.dto.IndexStatusRes;
import com.example.spring_rag.dto.RepositoryRes;
import com.example.spring_rag.entity.RepositoryEntity;
import com.example.spring_rag.security.UserSession;
import com.example.spring_rag.service.RepoService;
import com.example.spring_rag.service.indexing.IndexingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
public class RepoController {

    private final UserSession userSession;
    private final RepoService repoService;

    private final IndexingService indexingService;

    @GetMapping
    public ApiResponse<List<RepositoryRes>> list(
            @RequestParam(name = "refresh", defaultValue = "true") boolean refresh
    ) {

        UUID userId = userSession.getId();

        List<RepositoryRes> responseBody = refresh ? repoService.syncAndListRepos(userId) : repoService.listStored(userId);
        return ApiResponse.success(responseBody);
    }

    @GetMapping("/{id}")
    public ApiResponse<RepositoryRes> get(@PathVariable UUID id) {
        RepositoryEntity responseBody = repoService.requireOwned(id, userSession.getId());
        return ApiResponse.success(RepositoryRes.fromEntity(responseBody));
    }

    @PostMapping("/{id}/index")
    public ApiResponse<RepositoryRes> index(@PathVariable UUID id) {
        UUID userId = userSession.getId();
        RepositoryEntity repo = indexingService.startIndexing(id, userId);
        indexingService.indexAsync(id, userId);
        return ApiResponse.success(RepositoryRes.fromEntity(repo));
    }

    @GetMapping("/{id}/status")
    public ApiResponse<IndexStatusRes> status(@PathVariable UUID id) {
        IndexStatusRes responseBody = repoService.status(id, userSession.getId());
        return ApiResponse.success(responseBody);
    }

}
