package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> find(PageRequest pageRequest) {

		//Aqui eu busco todos aplicando o limite de paginação que o cliente definiu e lembre-se por padrão é LAZY LOADING, categoria não é carragada de primeira
		//Essa busca que o cliente recebe como resposta
		Page<Product> page = repository.findAll(pageRequest);

		//Essa busca é apenas para forçar o carregamento de categoria e depois fica em memória! Não retorno nada.
		//Com essa linha a classe ProductDTO ao fazer conversao de Category pra CategoryDTO não vai chamar o banco, pois ja tem em memoria.
		repository.findProductsCategories(page.stream().collect(Collectors.toList()));

		return page.map(x -> new ProductDTO(x));
	}
}
