
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.repository.NewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewService {

	@Autowired
	private NewRepository newRepo;


	@Transactional
	public int newCount() {
		return (int) this.newRepo.count();
	}
}
