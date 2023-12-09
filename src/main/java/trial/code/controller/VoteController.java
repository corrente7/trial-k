package trial.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import trial.code.dto.VoteDto;
import trial.code.service.VoteServiceImpl;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    @Autowired
    private VoteServiceImpl voteService;

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)

    public void createVote(@RequestBody VoteDto voteDto) {

        voteService.vote(voteDto);
    }
}
