package org.ong.pet.pex.backendpetx.dto.file;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class FotosBean {
    // nomeie como "arquivos" para ficar claro
    private List<MultipartFile> arquivos;

    public FotosBean() {}
    public FotosBean(List<MultipartFile> arquivos) { this.arquivos = arquivos; }

    public List<MultipartFile> getArquivos() { return arquivos; }
    public void setArquivos(List<MultipartFile> arquivos) { this.arquivos = arquivos; }
}
