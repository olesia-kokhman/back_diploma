package com.backenddiploma.controllers;


import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.services.charts.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchanger;

    @GetMapping
    public Double convert(
            @RequestParam double amount,
            @RequestParam Currency from,
            @RequestParam Currency to
    ) {
        return exchanger.convert(amount, from, to);
    }
}
