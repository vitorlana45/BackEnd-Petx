package org.ong.pet.pex.backendpetx;

import org.ong.pet.pex.backendpetx.entities.Animal;
import org.ong.pet.pex.backendpetx.entities.Ong;
import org.ong.pet.pex.backendpetx.enums.ComportamentoEnum;
import org.ong.pet.pex.backendpetx.enums.EspecieEnum;
import org.ong.pet.pex.backendpetx.enums.MaturidadeEnum;
import org.ong.pet.pex.backendpetx.enums.PorteEnum;
import org.ong.pet.pex.backendpetx.enums.SexoEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAsync
public class BackEndPetxApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BackEndPetxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		List<Animal> list = new ArrayList<Animal>();


		Ong ong = new Ong();


		Animal an = new Animal();
		// Atribuindo valores aos atributos do animal
		an.setNome("Fridis");
		an.setMaturidadeEnum(MaturidadeEnum.valueOf("IDOSO"));
		an.setRaca("Poodle");
		an.setSexoEnum(SexoEnum.FEMEA);
		an.setPorteEnum(PorteEnum.PEQUENO);
		an.setComportamentoEnum(ComportamentoEnum.DOCIL);
		an.setEspecieEnum(EspecieEnum.CACHORRO);


		list.add(an);

		LocalDateTime now = LocalDateTime.now();
		System.out.println("localDateTime  " + now);

		Instant is = Instant.now();
		System.out.println("Instant  " + is);

		// Exibindo os valores
		System.out.println("Nome: " + an.getNome());
		System.out.println("Idade: " + an.getMaturidadeEnum());
		System.out.println("Raça: " + an.getRaca());
		System.out.println("Sexo: " + an.getSexoEnum());
		System.out.println("Porte: " + an.getPorteEnum());
		System.out.println("Comportamento: " + an.getComportamentoEnum());
		System.out.println("Espécie: " + an.getEspecieEnum());
	}
}
