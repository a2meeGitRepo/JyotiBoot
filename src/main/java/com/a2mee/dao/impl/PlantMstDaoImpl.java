package com.a2mee.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.a2mee.dao.PlantMstDao;
import com.a2mee.model.Plant;

@Transactional
@Repository
public class PlantMstDaoImpl implements PlantMstDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Value(value = "${plantMstDao.getPlantList.hql}")
	private String plantMstDaoGetPlantListHql;
	
	@Value(value = "${plantMstDao.updatePlant.hql}")
	private String plantMstDaoUpdatePlantHql;
	
	@Override
	public List<Plant> getPlantList() {
		// TODO Auto-generated method stub
		List<Plant> plantList = entityManager.createQuery(plantMstDaoGetPlantListHql, Plant.class).getResultList();
		return plantList;
	}

	@Override
	public void addPlant(Plant plant) {
		// TODO Auto-generated method stub
		entityManager.persist(plant);
		entityManager.flush();
	}

	@Override
	public void deletePlant(Plant plant) {
		// TODO Auto-generated method stub
		entityManager.createQuery(plantMstDaoUpdatePlantHql)
		.setParameter("plant_id",plant.getPlant_id())
		.executeUpdate();
	}

}
