package org.ong.pet.pex.backendpetx;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Boletim;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.enums.*;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
import org.ong.pet.pex.backendpetx.repositories.BoletimRepository;
import org.ong.pet.pex.backendpetx.repositories.OngRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Instant;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
public class BackEndPetxApplication implements CommandLineRunner {

	private final OngRepository ongRepository;

	private final AnimalRepository animalRepository;

	private final BoletimRepository boletimRepository;

	public BackEndPetxApplication(OngRepository ongRepository, AnimalRepository animalRepository, BoletimRepository boletimRepository) {
		this.ongRepository = ongRepository;
		this.animalRepository = animalRepository;
        this.boletimRepository = boletimRepository;
    }

	public static void main(String[] args) {
		SpringApplication.run(BackEndPetxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ongRepository.count() == 0) {
			Ong ong = new Ong();
			ongRepository.save(ong);

			Animal an = new Animal();
			an.setNome("Fridis");
			an.setMaturidadeEnum(MaturidadeEnum.IDOSO);
			an.setRaca("Poodle");
			an.setSexoEnum(SexoEnum.FEMEA);
			an.setPorteEnum(PorteEnum.PEQUENO);
			an.setComportamento("Docil");
			an.setEspecieEnum(EspecieEnum.CACHORRO);
			an.setOng(ong); // Associar o animal à ONG
			animalRepository.save(an);

			Boletim boletim = new Boletim();
			boletim.setNumeroOcorrencia(123456L);
			boletim.setDataAtendimento(LocalDateTime.now());
			boletim.setMotivoRecolhimento("Abandono");
			boletim.setOrigem(OrigemAnimalEnum.ABANDONO);
			boletim.setNomeDenuncianteOuTutor("Vitor Lana");
			boletim.setCpfDenuncianteOuTutor("123.456.789-00");
			boletim.setTelefoneDenuncianteOuTutor("11987654321");
			boletim.setObservacaoClinica("Animal saudável");
			boletim.setRuaAvenida("Rua Exemplo");
			boletim.setCidade("São Paulo");
			boletim.setMunicipio("São Paulo");
			boletim.setBairro("Centro");
			boletim.setEstado("SP");
			boletim.setDestino(Destino.ADOTACAO);
			boletim.setAnimal(an); // Associar o boletim ao animal
			boletim.setOng(ong); // Associar o boletim à ONG
			boletimRepository.save(boletim);

			System.out.println("nome: " + an.getNome());
			System.out.println("maturidade: " + an.getMaturidadeEnum());
			System.out.println("raca: " + an.getRaca());
			System.out.println("sexo: " + an.getSexoEnum());
			System.out.println("porte: " + an.getPorteEnum());
			System.out.println("comportamento: " + an.getComportamento());
			System.out.println("especie: " + an.getEspecieEnum());
			System.out.println("Tutor: Vitor Lana");

			System.out.println("ONG e animais criados e salvos no banco de dados.");
		} else {
			System.out.println("ONG já existe no banco de dados.");
		}

		LocalDateTime now = LocalDateTime.now();
		System.out.println("localDateTime  " + now);

		Instant is = Instant.now();
		System.out.println("Instant  " + is);
	}
}