package io.github.dan7arievlis.marketplaceapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users/{id}/stocks")
@RequiredArgsConstructor
public class StockController implements GenericController {

}
