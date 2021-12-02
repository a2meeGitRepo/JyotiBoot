package com.a2mee.dao;

import java.util.List;

import com.a2mee.model.Plant;

public interface PlantMstDao {

	List<Plant> getPlantList();

	void addPlant(Plant plant);

	void deletePlant(Plant plant);

}
