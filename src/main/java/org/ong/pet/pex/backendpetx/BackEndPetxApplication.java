package org.ong.pet.pex.backendpetx;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.ong.pet.pex.backendpetx.repositories.AnimalRepository;
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

	public BackEndPetxApplication(OngRepository ongRepository, AnimalRepository animalRepository) {
		this.ongRepository = ongRepository;
		this.animalRepository = animalRepository;
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
			an.setComportamentoEnum(ComportamentoEnum.DOCIL);
			an.setEspecieEnum(EspecieEnum.CACHORRO);
			an.setOng(ong); // Associar o animal à ONG

			System.out.println("nome: " + an.getNome());
			System.out.println("maturidade: " + an.getMaturidadeEnum());
			System.out.println("raca: " + an.getRaca());
			System.out.println("sexo: " + an.getSexoEnum());
			System.out.println("porte: " + an.getPorteEnum());
			System.out.println("comportamento: " + an.getComportamentoEnum());
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