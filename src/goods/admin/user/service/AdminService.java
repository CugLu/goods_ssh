package goods.admin.user.service;

import goods.admin.user.dao.AdminDao;
import goods.admin.user.domain.Admin;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AdminService {
	@Resource
	private AdminDao adminDao;

	public Admin login(Admin admin) {
		return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
	}
}
