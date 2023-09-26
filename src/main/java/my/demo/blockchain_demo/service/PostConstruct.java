package my.demo.blockchain_demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostConstruct {
    private final BlockchainAccessor accessor;

    @jakarta.annotation.PostConstruct
    public void init() throws Exception {
        accessor.go();
    }
}
