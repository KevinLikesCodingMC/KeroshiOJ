package org.keroshi.keroshioj.service;

import org.keroshi.keroshioj.config.OjProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
	private final OjProperties ojProperties;
	public FileService(OjProperties ojProperties) {
		this.ojProperties = ojProperties;
	}

	public void saveProblemZip(long id, MultipartFile file) throws IOException {
		Path target = Paths.get(ojProperties.getDataPath(), "problem", String.valueOf(id))
				.toAbsolutePath().normalize();
		System.out.println(target);
	}
}
