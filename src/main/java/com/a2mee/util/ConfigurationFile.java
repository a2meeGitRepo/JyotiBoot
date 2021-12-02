package com.a2mee.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class ConfigurationFile {
	private String exprireDate;

	public String getExprireDate() {
		return exprireDate;
	}

	public void setExprireDate(String exprireDate) {
		this.exprireDate = exprireDate;
	}

}
