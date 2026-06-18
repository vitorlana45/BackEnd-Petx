package org.ong.pet.pex.backendpetx.dto.file;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FotosBean {
    // nomeie como "arquivos" para ficar claro
    private List<MultipartFile> arquivos;

    public FotosBean() {}
    public FotosBean(List<MultipartFile> arquivos) { this.arquivos = arquivos; }

}
