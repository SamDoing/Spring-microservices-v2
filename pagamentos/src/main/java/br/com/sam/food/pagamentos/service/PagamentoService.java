package br.com.sam.food.pagamentos.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.sam.food.dto.PagamentoDto;
import br.com.sam.food.pagamentos.model.Pagamento;
import br.com.sam.food.pagamentos.model.Status;
import br.com.sam.food.pagamentos.repository.PagamentoRepository;

@Service
public class PagamentoService {

	@Autowired
	PagamentoRepository repository;

	@Autowired
	ModelMapper modelMapper;

	public Page<PagamentoDto> obterTodos(Pageable paginacao) {
		return repository
				.findAll(paginacao)
				.map( p ->  modelMapper.map( p, PagamentoDto.class ));
	}
	
	public PagamentoDto obterPorId(Long id) {
		return modelMapper.map(repository.findById(id).orElse(null), PagamentoDto.class);
	}
	
	public PagamentoDto criaPagamento(PagamentoDto pagamentoDto) {
		
		Pagamento pagamento = modelMapper.map(pagamentoDto, Pagamento.class);
		pagamento.setStatus(Status.CRIADO);
		repository.save(pagamento);
		
		return modelMapper.map(pagamento, PagamentoDto.class);
	}
	
	public PagamentoDto atualizaPagamento(Long id, PagamentoDto pagamentoDto) {
		
		if( repository.findById(id).isEmpty() )
			return null;
		
		pagamentoDto.setId(id);
		repository.save( modelMapper.map(pagamentoDto, Pagamento.class) );
		
		return pagamentoDto;
	}
	
	public Boolean deletaPagamento(Long id) {
		if( repository.findById(id).isEmpty() )
			return false;
		
		repository.deleteById(id);
		return true;
	}
}
