package com.a2mee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.a2mee.dao.DashboardModelPlanDto;
import com.a2mee.model.AssemblyMst;
import com.a2mee.model.ComponentMst;
import com.a2mee.model.ItemMst;
import com.a2mee.model.MaterialTransaction;
import com.a2mee.model.MtlStockIn;
import com.a2mee.model.StockItem;
import com.a2mee.model.dto.DashboardMaterialStock;
import com.a2mee.services.DashBoardService;
import com.a2mee.util.API;

@RestController
@RequestMapping(API.dashboard)
@CrossOrigin("*")
public class DashBoardController {
	
	@Autowired
	DashBoardService dashBoardService;
	
	
	@GetMapping(API.dashboardModelPlans)
	public @ResponseBody List<DashboardModelPlanDto> getDashboardModelPlans() {
		try {
			List<DashboardModelPlanDto> list= dashBoardService.getDashboardModelPlans();
			return list ;
		} catch (Exception e) {
			return  null;
		}
	}
	
	

	@GetMapping(API.todaysTranction)
	public @ResponseBody List<MaterialTransaction> gettodaysTranction() {
		try {
			List<MaterialTransaction> list= dashBoardService.gettodaysTranction();
			return list ;
		} catch (Exception e) {
			return  null;
		}
	}
	
	@GetMapping(API.materialLocationWiseStock)
	public @ResponseBody List<DashboardMaterialStock>  materialLocationWiseStock(@RequestParam("pageno") int pageno,@RequestParam("perPage") int perPage) {
		try {
			List<DashboardMaterialStock> list= dashBoardService.getMaterialStock(pageno,perPage);
			System.out.println("Count "+list.size());
			return list ;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}
	
	@GetMapping(API.materialLocationWiseStockCount)
	public @ResponseBody int materialLocationWiseStockCount() {
		try {
			int count= dashBoardService.materialStockCount();
			return count ;
		} catch (Exception e) {
			e.printStackTrace();
			return  0;
		}
	}
	
	
	@GetMapping(API.materialLocationWiseStockSearch)
	public @ResponseBody List<DashboardMaterialStock> materialLocationWiseStockSearch(@RequestParam("pageno") int pageno,@RequestParam("perPage") int perPage,@RequestParam("search") String search) {
		try {
			System.out.println("Page N0 : "+pageno);
			List<DashboardMaterialStock> list= dashBoardService.materialStockBySearch(pageno,perPage,search);
			return list ;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}
	
	@GetMapping(API.materialLocationWiseStockSearchCount)
	public @ResponseBody long materialLocationWiseStockSearchCount(@RequestParam("search") String search) {
		try {
			long count= dashBoardService.materialStockCountBySearch(search);
			return count ;
		} catch (Exception e) {
			e.printStackTrace();
			return  0;
		}
	}
	
	
	
	
	
	
	@GetMapping(API.materialStock)
	public @ResponseBody List<DashboardMaterialStock> getMaterialStock(@RequestParam("pageno") int pageno,@RequestParam("perPage") int perPage) {
		try {
			List<DashboardMaterialStock> list= dashBoardService.getMaterialStock(pageno,perPage);
			return list ;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}
	@GetMapping(API.materialStockCount)
	public @ResponseBody int materialStockCount() {
		try {
			int count= dashBoardService.materialStockCount();
			return count ;
		} catch (Exception e) {
			e.printStackTrace();
			return  0;
		}
	}
	

	@GetMapping(API.materialStockBySearch)
	public @ResponseBody List<DashboardMaterialStock> materialStockBySearch(@RequestParam("pageno") int pageno,@RequestParam("perPage") int perPage,@RequestParam("search") String search) {
		try {
			List<DashboardMaterialStock> list= dashBoardService.materialStockBySearch(pageno,perPage,search);
			return list ;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}
	@GetMapping(API.materialStockCountBySearch)
	public @ResponseBody long materialStockCountBySearch(@RequestParam("search") String search) {
		try {
			long count= dashBoardService.materialStockCountBySearch(search);
			return count ;
		} catch (Exception e) {
			e.printStackTrace();
			return  0;
		}
	}


}
