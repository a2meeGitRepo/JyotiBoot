package com.a2mee.util;

public class API {

	public static final String test = "test";

	/* ...GRN Controller... */

	public static final String grn = "grn";
	public static final String createGrn = "/createGrn";
	public static final String submitGrn = "/submitGrn";
	public static final String serchByPOno = "/serchByPOno";
	public static final String itemlist = "/itemlist";
	public static final String venlist = "/venlist";
	public static final String updateQr = "/updateQr";
	public static final String scanQr = "/scanQr";
	public static final String rejectResonList = "/rejectResonList";
	public static final String inspectMateril = "/inspectMateril";	
	public static final String GRNReports = "/GRNReports";
	public static final String assemblyOlReports = "/assemblyOlReports";
	public static final String barcodeTranervesReports = "/barcodeTranervesReports";

	public static final String grnItemList = "/grnItemList";
	public static final String venGrnlist = "/venGrnlist";
	public static final String itemGrnlist = "/itemGrnlist";
	public static final String updateSapData = "/updateSapData";
	public static final String deleteGrn = "/deleteGrn";
	
	/* ...Purchase Order Controller.....*/
	public static final String purchaseOrder = "purchaseOrder";
	public static final String submitPurchaseOrder = "/submitPurchaseOrder";
	public static final String searchByPurchaseOrderno = "/searchByPurchaseOrderno";
	public static final String searchByPurchaseOrderId = "/searchByPurchaseOrderId";
	public static final String inwardedPo = "/inwardedPo";
	public static final String updateList = "/updateList";
	public static final String poItemList = "/purchaseOrderItemList";
	public static final String poVenList = "/purchaseOrderVenList";
	public static final String errorList = "/errorList";
	public static final String poDeviations = "/poDeviations";
	public static final String submitNoErrPurchaseOrder = "/noErrorPo";
	public static final String searchByNoErrPurchaseOrderno = "/noErrorPoNo";
	public static final String submitErrPurchaseOrder = "/devPo";
	public static final String searchByErrPurchaseOrderno = "/devPoNo";
	public static final String getErrorlistByPoItemID = "/getErrorlistByPoItemID";
	public static final String updateDeviations = "/updateDeviations";
	public static final String delDeviations = "/delDeviations";
	public static final String uploadCsvPurchaseOrder ="uploadCsvPurchaseOrder";
	public static final String getPurchaseOrders = "/getPurchaseOrders";	
	public static final String getTotalCount = "/getTotalCount";
	
	/*.........Location.......Anurag */
	public static final String putAway = "/putAway";
	public static final String storageBinList = "/storageBinList";
	public static final String addStorageBin = "/addStorageBin";
	public static final String delStorageBin = "/delStorageBin";
	public static final String itemList = "/itemList";
	public static final String addItemLocMap = "/addItemLocMap";
	public static final String mapItemToStorageBin = "/mapItemToStorageBin";
	public static final String storeItem = "/storeItem";
	public static final String itemLocMapList = "/itemLocMapList";	
	public static final String acceptedList = "/acceptedList";

	public static final String getStockList = "getStockList";
	public static final String addPlant = "addPlant";
	public static final String listPlant = "listPlant";
	public static final String deletePlant = "deletePlant";
	public static final String addStorage = "addStorage";
	public static final String listStorage = "listStorage";
	public static final String deleteStorage = "deleteStorage";

	public static final String listItems = "/listItems";	
	public static final String binItemCheck = "/binItemCheck";
	public static final String getStockListByItem = "/getStockListByItem";
	public static final String delItemLocMap = "/delItemLocMap";
	public static final String uploadCsvStorageBins = "/uploadCsvStorageBins";
	public static final String uploadOldStock = "/uploadOldStock";
	public static final String getAssingStorageLocByItem = "/getAssingStorageLocByItem";

	
	
	/*.........Bom.......Anurag */
	public static final String bom = "/bom";
	public static final String uploadBom = "/uploadBom";
	public static final String modelList = "/modelList";
	
	
	/*.........Model Plan.......Anurag */
	public static final String modelPlan = "/modelPlan";
	public static final String uploadModelPlan = "/uploadModelPlan";
	public static final String addModelPlan = "/addModelPlan";
	public static final String addModelPlans = "/addModelPlans";
	public static final String updateModelPlan = "/updateModelPlan";
	public static final String modelPlanList = "/modelPlanList";
	public static final String getModelByMonth = "/getModelByMonth";
	public static final String getAssmByModel = "/getAssmByModel";
	public static final String getCompByAssm = "/getCompByAssm";
	public static final String getAssmCountByMonth = "/getAssmCountByMonth";
	public static final String getCompCountByMonth = "/getCompCountByMonth";
	public static final String getAssmByMonth = "/getAssmByMonth";
	public static final String getCompByMonth = "/getCompByMonth";
	public static final String assignPicker = "/assignPicker";
	public static final String getPendingAssms = "/getPendingAssms";
	public static final String getAssms = "/getAssms";
	public static final String getAssmsByModelName = "/getAssmsByModelName";
	public static final String getAssmsByUserAndModel = "/getAssmsByUserAndModel";
	public static final String getCompsByAssm = "/getCompsByAssm";
	public static final String getStockByComp = "/getStockByComp";	
	public static final String updateStocks = "/updateStocks";
	public static final String getKitStock = "/getKitStock";
	public static final String getAssmsByPOModelCode = "getAssmsByPOModelCode";
	public static final String getCompsByAssmCodePO = "getCompsByAssmCodePO";
	public static final String getCompsByAssmCodePO2 = "getCompsByAssmCodePO2";
	public static final String getLocationWiseCompsByAssmCodePO = "getLocationWiseCompsByAssmCodePO";
	
	/*.........Model Plan.......Praful */
	public static final String pickingByPrdOrdNo = "pickingByPrdOrdNo";
	public static final String pickingBySalesOno = "pickingBySalesOno";
	public static final String getPickingAssemblyByPicking = "getPickingAssemblyByPicking";
	public static final String getPickingAssemblyDtoByPicking = "getPickingAssemblyDtoByPicking";
	public static final String updateAllPickingAssembly = "updateAllPickingAssembly";
	public static final String getPickingComponentDtoByPickingAssm = "getPickingComponentDtoByPickingAssm";
	public static final String getAssmsByPOPicking = "getAssmsByPOPicking";
	public static final String getPickingPOByComp = "getPickingPOByComp";
	public static final String getPickingComponentByPickingAndAssemblyAndComponent = "getPickingComponentByPickingAndAssemblyAndComponent";
	public static final String getPOByComp = "/getPOByComp";
	public static final String getPickingComponentByPickingAssembly = "/getPickingComponentByPickingAssembly";
	public static final String updateAllPickingComponent = "updateAllPickingComponent";
	public static final String modelsInPicking = "modelsInPicking";
	public static final String POPickingByModel = "POPickingByModel";
	public static final String uploadReservationItemNo = "uploadReservationItemNo";
	
	/*.........Production Order.......Anurag */
	public static final String proOrder = "/proOrder";
	public static final String uploadProOrder = "/uploadProOrder";
	public static final String getProOrdNo = "/getProOrdNo/{modelCode}";
	public static final String salesOrderlist = "/salesOrderlist";
	public static final String serchByProOno = "/serchByProOno";
	public static final String getAssmByModelCode = "/getAssmByModelCode";
	public static final String getPOByModelCode = "/getPOByModelCode";
	public static final String approved = "/approved";
	public static final String getAssemblies = "/getAssemblies";
	public static final String createCustomAssm = "/createCustomAssm";
	public static final String getApprovedPo = "/getApprovedPo";
	public static final String getReqAssmsByPo = "/getReqAssmsByPo";
	public static final String getCustomAssembly = "/getCustomAssembly";
	public static final String getReqAssms = "/getReqAssms";
	public static final String createReqAssm = "/createReqAssm";
	public static final String createReqAssms = "/createReqAssms";
	public static final String assmReceived = "/assmReceived";
	public static final String consumeAssm = "/consumeAssm";
	public static final String getRequestedAssms = "/getRequestedAssms";
	public static final String getComponents = "getComponents";
	
	/* ........User/Role/Permission....... */
	
	        /* ........User....... */
	public static final String user= "user";
	public static final String userpost= "/userpost";
	public static final String userGetAll = "/alluser";
	public static final String userGetOne = "/{id}";
	public static final String getUserPermission = "/permissions/{id}";
	public static final String getUserPermissionMobile = "/permissions/mobile/{id}";
	public static final String deleteUser = "/{id}";
	public static final String assignRoles = "/assignroles";
	public static final String deleteRoles = "/deleteroles";
	public static final String getUserRoles = "/userroles/{id}";
	public static final String userCheck = "/usercheck/{id}";
	public static final String editUser = "/edit";
	
	
	           /* ........Role....... */
	public static final String roles = "roles";
	public static final String addRole = "";
	public static final String getAllRole = "/allRoles";
	public static final String getAllRoleDetails = "/allRoleDetails";
	public static final String getOneRole = "/{id}";
	public static final String deleteRole = "/{id}";
	public static final String assignUser = "/assignUser";
	public static final String removeUser = "/removeUser";
	public static final String assignPermissions = "/assignPermissions";
	public static final String getUsers = "/getusers/{id}";
	public static final String activate = "/activate";
	
	
                  /* ........Permissions....... */
	public static final String permission = "permission";
	public static final String addPermission = "";
	public static final String getAllPermissions = "/allPermissions";
	public static final String getOnePermission = "/{id}";
	public static final String deletePermission = "/{id}";
	
	                   /* ...Login..... */
	public static final String login = "login";
	public static final String authenticate = "/authenticate";
	
        
	// --------------- | Report | ----------
	public static final String report ="/report";
		/*----Anurag---*/
	public static final String listStocks = "/listStocks";
	public static final String listStocksByDate = "/listStocksByDate";
	public static final String todaysGrn = "/todaysGrn";
	public static final String grnByDateRange = "/grnByDateRange";
	public static final String todaysDeviation = "/todaysDeviation";
	public static final String DeviationByDateRange = "/DeviationByDateRange";
	public static final String todaysPutAway = "/todaysPutAway";
	public static final String putAwayByDateRange = "/putAwayByDateRange";
	public static final String getTotalComplCount = "/getTotalComplCount";
	public static final String serchBySalesOno = "/serchBySalesOno";
	public static final String listPickings = "listPickings";
	public static final String listPickingsByDate = "listPickingsByDate";
	public static final String listTransaction = "listTransaction";
	public static final String listTransactionDateRange = "listTransactionDateRange";
	public static final String listPending = "listPending";
	public static final String listShort = "listShort";
	public static final String getPendingReport = "/getPendingReport";
	
	/************ Dattatray*******************/
	public static final String suuplier = "suuplier";
	public static final String items = "items";
	
	public static final String updateitem = "/updateitem";
	public static final String getProductionOrders = "/getProductionOrders";
	public static final String updateProductionOrder = "/updateProductionOrder";
	public static final String updateStatusProductionOrder = "/updateStatusProductionOrder";
	public static final String getSupplierList = "/getSupplierList";
	public static final String getSupplierComponantsBySupplier = "/getSupplierComponantsBySupplier";
	public static final String addSupplierComponant = "/addSupplierComponant";
	public static final String getSupplierComponantsByItem = "/getSupplierComponantsByItem";
	public static final String uploadItems = "/uploadItems";
	public static final String uploadSupplierItems = "/uploadSupplierItems";
	public static final String getDetialPendingReport = "/getDetialPendingReport";
	public static final String updatePendingReport = "/updatePendingReport";
	public static final String getsupplierByComponantCode = "/getsupplierByComponantCode";
	public static final String getPendingList2 = "/getPendingList2Report";
	public static final String pendingReportManualCall = "pendingReportManualCall";


	
	//************ Dash b[ard Controller***************//
		public static final String dashboard="dashboard";
		public static final String dashboardModelPlans = "/dashboardModelPlans";
		public static final String materialStock = "/materialStock";
		public static final String materialStockCount = "/materialStockCount";

		public static final String materialStockBySearch = "/materialStockBySearch";
		
		public static final String materialStockCountBySearch = "/materialStockCountBySearch";

		public static final String materialLocationWiseStockCount = "/materialLocationWiseStockCount";
		public static final String materialLocationWiseStockSearchCount = "/materialLocationWiseStockSearchCount";
		public static final String todaysTranction = "/todaysTranction";
		
		
		
		
		
		public static final String materialLocationWiseStock = "/materialLocationWiseStock";
		public static final String materialLocationWiseStockSearch = "/materialLocationWiseStockSearch";
		
		public static final String testAPI = "testAPI";

		public static final String testAPISingle = "testAPISingle";

		public static final String updateLocationWiseComponent = "updateLocationWiseComponent";

		public static final String getTotalStorageLocationCount = "getTotalStorageLocationCount";
		public static final String storageBinItemWiseListPagination = "storageBinItemWiseListPagination/{pageno}/{perPage}";

		public static final String getTotalItemCount = "getTotalItemCount";

		public static final String itemListByPagination = "itemListByPagination/{pageno}/{perPage}";

		public static final String getTotalPOCount = "getTotalPOCount";
		public static final String getProductionOrdersByPagination = "getProductionOrdersByPagination/{pageno}/{perPage}";

	
}
	
