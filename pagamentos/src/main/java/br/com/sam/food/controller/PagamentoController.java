package br.com.sam.food.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sam.food.dto.PagamentoDto;
import br.com.sam.food.pagamentos.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {
	
	@Autowired
	PagamentoService service;
	
	@GetMapping
	public Page<PagamentoDto> obterTodosPagamentos(@PageableDefault(size = 10) Pageable paginacao) {
		return service.obterTodos(paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PagamentoDto> obterPagamento(@PathVariable @NotNull Long id) {
		
		PagamentoDto pagamentoDto = service.obterPorId(id);
		if ( pagamentoDto == null)
			return new ResponseEntity<PagamentoDto>(HttpStatus.NO_CONTENT);
		
		return ResponseEntity.ok(pagamentoDto);
	}
	
	@PostMapping
	public ResponseEntity<PagamentoDto> criaPagamento(@RequestBody @Valid PagamentoDto pagamentoDto) throws URISyntaxException {
		
		service.criaPagamento(pagamentoDto);
		URI uri = new URI( "pagamento/%d".formatted(pagamentoDto.getId() ));
		
		return ResponseEntity.created(uri).body(pagamentoDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PagamentoDto> atualizaPagamento(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto pagamento) {
		
		PagamentoDto pagamentoDto = service.atualizaPagamento(id, pagamento);
		if ( pagamentoDto == null)
			return new ResponseEntity<PagamentoDto>(pagamentoDto, HttpStatus.NOT_FOUND);
		
		return ResponseEntity.ok(pagamentoDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<PagamentoDto> deletaPagamento(@PathVariable @NotNull Long id) {
		
		
		if ( service.deletaPagamento(id) )
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.noContent().build();
	}
}
