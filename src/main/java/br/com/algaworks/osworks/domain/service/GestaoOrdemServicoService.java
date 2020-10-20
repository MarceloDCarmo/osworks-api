package br.com.algaworks.osworks.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.algaworks.osworks.api.model.Comentario;
import br.com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import br.com.algaworks.osworks.domain.exception.NegocioException;
import br.com.algaworks.osworks.domain.model.Cliente;
import br.com.algaworks.osworks.domain.model.OrdemServico;
import br.com.algaworks.osworks.domain.model.StatusOrdemServico;
import br.com.algaworks.osworks.domain.repository.ClienteRepository;
import br.com.algaworks.osworks.domain.repository.ComentarioRepository;
import br.com.algaworks.osworks.domain.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ComentarioRepository comentarioRepository;
	
	public OrdemServico criar (OrdemServico ordemServico) {
		
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("Cliente não encontrado"));
		
		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());
		
		return ordemServicoRepository.save(ordemServico);
	}
	
	public void finalizar(Long ordemServicoId) {
	
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.finalizar();
		ordemServicoRepository.save(ordemServico);	
	}

	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		Comentario comentario = new Comentario(ordemServico, descricao);
	
		return comentarioRepository.save(comentario);
	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
		.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrada!"));
	}
}
