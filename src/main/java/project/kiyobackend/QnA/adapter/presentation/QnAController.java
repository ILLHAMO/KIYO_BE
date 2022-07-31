package project.kiyobackend.QnA.adapter.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import project.kiyobackend.QnA.adapter.presentation.dto.QnASaveRequest;
import project.kiyobackend.QnA.application.QnAService;
import project.kiyobackend.auth.entity.CurrentUser;
import project.kiyobackend.user.domain.User;

@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnAController {

    private final QnAService qnAService;

    @GetMapping
    public void getQnAList(@CurrentUser User user, @RequestParam(name = "lastStoreId",required = false) Long lastStoreId, Pageable pageable)
    {

    }

    @PostMapping
    public void addQnA(@RequestBody QnASaveRequest qnASaveRequest)
    {

    }

    @DeleteMapping("/{qnaId}")
    public void removeQnA()
    {

    }

    @PutMapping("/{qnaId}")
    public void updateQnA()
    {

    }
}
