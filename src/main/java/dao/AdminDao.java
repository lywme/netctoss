package dao;

import entity.Admin;

public interface AdminDao {
	Admin findByCode(String code);
}

