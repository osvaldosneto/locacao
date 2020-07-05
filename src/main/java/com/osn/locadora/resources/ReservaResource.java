package com.osn.locadora.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.osn.locadora.domain.Reserva;
import com.osn.locadora.dto.ReservaDTO;
import com.osn.locadora.dto.ReservaNewDTO;
import com.osn.locadora.services.ReservaService;

@RestController
@RequestMapping(value = "/reservas")
public class ReservaResource {

	@Autowired
	private ReservaService service;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ReservaDTO>> findAll() {
		List<Reserva> listaCli = service.findAll();
		List<ReservaDTO> listaDTO = new ArrayList<ReservaDTO>();
		for (Reserva r : listaCli) {
			ReservaDTO cliDTO = new ReservaDTO(r);
			listaDTO.add(cliDTO);
		}
		return ResponseEntity.ok().body(listaDTO);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Long id) {
		ReservaDTO objDTO = service.findId(id);
		return ResponseEntity.ok().body(objDTO);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ReservaNewDTO objDTO) {
		Reserva obj = service.fromNewDTO(objDTO);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/nova", method = RequestMethod.POST)
	public ResponseEntity<Void> insertNova(@Valid @RequestBody ReservaDTO objDTO) {
		Reserva obj = service.fromDTO(objDTO);
		obj = service.insertNova(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ReservaDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "now") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {

		Page<Reserva> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ReservaDTO> listDto = list.map(obj -> new ReservaDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}