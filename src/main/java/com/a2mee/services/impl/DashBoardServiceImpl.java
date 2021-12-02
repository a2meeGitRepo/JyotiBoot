package com.a2mee.services.impl;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.a2mee.dao.DashboardModelPlanDto;
import com.a2mee.dao.ItemMstDao;
import com.a2mee.dao.MaterialTransactionRepo;
import com.a2mee.dao.ModelPlanDao;
import com.a2mee.dao.MtlStockInDao;
import com.a2mee.dao.StockItemDao;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialTransaction;
import com.a2mee.model.ModelPlan;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StockItem;
import com.a2mee.model.dto.DashboardMaterialStock;
import com.a2mee.services.DashBoardService;
@Service
public class DashBoardServiceImpl implements DashBoardService {
	@Autowired
	ModelPlanDao modelPlanDao;
	@Autowired
	ItemMstDao itemMstDao;
	@Autowired
	StockItemDao stockItemDao;
	@Autowired
	MtlStockInDao mtlStockInDao;
	@Autowired
	MaterialTransactionRepo materialTransactionRepo;
	
	
	 String getMonthForInt(int num) {
	        String month = "wrong";
	        DateFormatSymbols dfs = new DateFormatSymbols();
	        String[] months = dfs.getMonths();
	        if (num >= 0 && num <= 11 ) {
	            month = months[num];
	        }
	        return month;
	    }
	@Override
	public List<DashboardModelPlanDto> getDashboardModelPlans() {
		// TODO Auto-generated method stub
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = localDate.getMonthValue();
		String lastMonthName=getMonthForInt(month-2);
		String currMonthName=getMonthForInt(month-1);
		String nextMonthName=getMonthForInt(month);

	
		int year=localDate.getYear();
		
		Set<String> modelList= new HashSet<String>();
		if(currMonthName.substring(0,3).toUpperCase().equalsIgnoreCase("DEC")){
			//modelPlans=modelPlanDao.getModelByLCNMonth(lastMonthName.substring(0,3).toUpperCase(),currMonthName.substring(0,3).toUpperCase(),nextMonthName.substring(0,3).toUpperCase(), String.valueOf(year),String.valueOf(year),String.valueOf(year+1));

			
			
			List<ModelPlan> modelPlans0=modelPlanDao.getModelByMonth(lastMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			List<ModelPlan> modelPlans1=modelPlanDao.getModelByMonth(currMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			List<ModelPlan> modelPlans2=modelPlanDao.getModelByMonth(nextMonthName.substring(0,3).toUpperCase(), String.valueOf(year+1));
			 for(ModelPlan modelPlan:modelPlans0){
				 modelList.add(modelPlan.getModelCode());
			 }
			 for(ModelPlan modelPlan:modelPlans1){
					 modelList.add(modelPlan.getModelCode());
								 
			}
			 for(ModelPlan modelPlan:modelPlans2){
					 modelList.add(modelPlan.getModelCode());
			}

				
		}
		else if(currMonthName.substring(0,3).toUpperCase().equalsIgnoreCase("JAN")){
			
			//modelPlans=modelPlanDao.getModelByLCNMonth(lastMonthName.substring(0,3).toUpperCase(),currMonthName.substring(0,3).toUpperCase(),nextMonthName.substring(0,3).toUpperCase(), String.valueOf(year-1),String.valueOf(year),String.valueOf(year));

			List<ModelPlan> modelPlans0=modelPlanDao.getModelByMonth(lastMonthName.substring(0,3).toUpperCase(), String.valueOf(year-1));
			List<ModelPlan> modelPlans1=modelPlanDao.getModelByMonth(currMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			List<ModelPlan> modelPlans2=modelPlanDao.getModelByMonth(nextMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
		
			 for(ModelPlan modelPlan:modelPlans0){
				 modelList.add(modelPlan.getModelCode());
			 }
			 for(ModelPlan modelPlan:modelPlans1){
					 modelList.add(modelPlan.getModelCode());
								 
			}
			 for(ModelPlan modelPlan:modelPlans2){
					 modelList.add(modelPlan.getModelCode());
			}
			 
			 
		  }else{
			List<ModelPlan> modelPlans0=modelPlanDao.getModelByMonth(lastMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			List<ModelPlan> modelPlans1=modelPlanDao.getModelByMonth(currMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			List<ModelPlan> modelPlans2=modelPlanDao.getModelByMonth(nextMonthName.substring(0,3).toUpperCase(), String.valueOf(year));
			
			 for(ModelPlan modelPlan:modelPlans0){
				 modelList.add(modelPlan.getModelCode());
			 }
			 for(ModelPlan modelPlan:modelPlans1){
					 modelList.add(modelPlan.getModelCode());
								 
			}
			 for(ModelPlan modelPlan:modelPlans2){
					 modelList.add(modelPlan.getModelCode());
			}
			 
			 
		}
		System.out.println("MODEL LIST "+modelList.size());
		
		return null;
	}
	/*@Override
	public List<DashboardMaterialStock> getMaterialStock(int pageno, int perPage) {
		// TODO Auto-generated method stub
		List<DashboardMaterialStock> retrunList=new ArrayList<DashboardMaterialStock>();
		List<ItemMst> items=itemMstDao.getEntireItemMst();
		System.out.println("ITEM SIZE "+items.size());
		for (ItemMst  itemMst: items){
			double quanityt=mtlStockInDao.getTotalStockByItem(itemMst.getId());
			System.out.println("ITEM :  "+itemMst.getId()+" Quantity : "+quanityt);
			DashboardMaterialStock dashboardMaterialStock= new DashboardMaterialStock();
			dashboardMaterialStock.setItemMst(itemMst);
			dashboardMaterialStock.setQuantiry(quanityt);
			retrunList.add(dashboardMaterialStock);
		}
		
		
		return retrunList;
	}*/
	@Override
	public List<MtlStockIn> materialLocationWiseStock(int pageno, int perPage) {
		// TODO Auto-generated method stub
	List<MtlStockIn> list=mtlStockInDao.getMaterialStock(pageno,perPage);
		
		return list;
	}
	
	@Override
	public List<MtlStockIn> materialLocationWiseStockSearch(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		List<MtlStockIn> list=mtlStockInDao.materialLocationWiseStockSearch(pageno,perPage,search);
		return list;
	}

	/*@Override
	public List<MtlStockIn> materialStockBySearch(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		List<MtlStockIn> list=mtlStockInDao.materialStockBySearch(pageno,perPage,search);

		return list;
	}*/
	@Override
	public int materialLocationWiseStockCount() {
		// TODO Auto-generated method stub
		return mtlStockInDao.materialLocationWiseStockCount();
	}
	@Override
	public int materialLocationWiseStockSearchCount(String search) {
		// TODO Auto-generated method stub
		return mtlStockInDao.materialLocationWiseStockSearchCount(search);
	}
	@Override
	public List<DashboardMaterialStock> getMaterialStock(int pageno, int perPage) {
		// TODO Auto-generated method stub
		List<DashboardMaterialStock> retrunList=new ArrayList<DashboardMaterialStock>();
		List<ItemMst> itemsByPagination=itemMstDao.getItemMstByPagination(pageno,perPage);

		List<ItemMst> items=itemMstDao.getEntireItemMst();
		System.out.println("ITEM SIZE "+items.size());
		for (ItemMst  itemMst: itemsByPagination){
			String quanityt=mtlStockInDao.getTotalStockByItem(itemMst.getId());
			System.out.println("ITEM :  "+itemMst.getId()+" Quantity : "+quanityt);
			DashboardMaterialStock dashboardMaterialStock= new DashboardMaterialStock();
			dashboardMaterialStock.setItemMst(itemMst);
			if(quanityt==null){
				dashboardMaterialStock.setQuantiry("0");
			}else{
				dashboardMaterialStock.setQuantiry(quanityt);
			}
			//dashboardMaterialStock.setQuantiry(quanityt);
			retrunList.add(dashboardMaterialStock);
		}
		
		
		return retrunList;
	}
	@Override
	public int materialStockCount() {
		// TODO Auto-generated method stub
		List<ItemMst> items=itemMstDao.getEntireItemMst();

		return items.size();
	}
	@Override
	public List<DashboardMaterialStock> materialStockBySearch(int pageno, int perPage, String search) {
		// TODO Auto-generated method stub
		List<DashboardMaterialStock> retrunList=new ArrayList<DashboardMaterialStock>();

		List<ItemMst> itemsByPagination=itemMstDao.getItemMstByPagination(pageno,perPage,search);
		for (ItemMst  itemMst: itemsByPagination){
			String quanityt=mtlStockInDao.getTotalStockByItem(itemMst.getId());
			System.out.println("ITEM :  "+itemMst.getId()+" Quantity : "+quanityt);
			DashboardMaterialStock dashboardMaterialStock= new DashboardMaterialStock();
			dashboardMaterialStock.setItemMst(itemMst);
			if(quanityt==null){
				dashboardMaterialStock.setQuantiry("0");
			}else{
				dashboardMaterialStock.setQuantiry(quanityt);
			}
			retrunList.add(dashboardMaterialStock);
		}
		return retrunList;
	}
	@Override
	public long materialStockCountBySearch(String search) {
		// TODO Auto-generated method stub
		return itemMstDao.materialStockCountBySearch(search);
	}
	@Override
	public List<MaterialTransaction> gettodaysTranction() {
		// TODO Auto-generated method stub
		return materialTransactionRepo.gettodaysTranction(new Date());
	}
	
}
