##############################  设备管理API  START  ##############################

-------------------------- 设备厂商 --------------------------

### 1. 设备厂商接口-查询所有厂商 有分页（列表）

###### URL
/deviceFirms/selectDeviceFirmsListPage
###### API Type
POST
###### params
{
    firmName : ""	        // 厂商名称 String  
    pageSize
    currentsPage
}
###### return
{
    
}

### 1.1 设备厂商接口-查询所有厂商（列表）

###### URL
/deviceFirms/getDeviceFirms
###### API Type
POST
###### params
{

}
###### return
{
    
}

### 2. 设备厂商接口-添加设备厂商

###### URL
/deviceFirms/insertDeviceFirm
###### API Type
POST
###### params
{
    firmName : ""	        			// 厂商名称 
    firmDesc : ""	        			// 厂商描述  
    firmAdress : ""	        			// 厂商地址  
	firmPostcode : ""	        		// 厂商邮编  
	firmContact : ""	        		// 厂商联系人  
	firmPhone : ""	        			// 厂商联系方式  
	firmResponsible : ""	        	// 本部联系厂商负责人  
	firmResponsiblePhone : ""	        // 本部联系厂商负责人联系方式  
}
###### return
{
    
}

### 3. 设备厂商接口-修改设备厂商

###### URL
/deviceFirms/updateDeviceFirm
###### API Type
POST
###### params
{
	id : ""
    firmName : ""	        			// 厂商名称 
    firmDesc : ""	        			// 厂商描述  
    firmAdress : ""	        			// 厂商地址  
	firmPostcode : ""	        		// 厂商邮编  
	firmContact : ""	        		// 厂商联系人  
	firmPhone : ""	        			// 厂商联系方式  
	firmResponsible : ""	        	// 本部联系厂商负责人  
	firmResponsiblePhone : ""	        // 本部联系厂商负责人联系方式  
}
###### return
{
    
}

### 4. 设备厂商接口-删除设备厂商

###### URL
/deviceFirms/deleteDeviceFirm
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 5. 设备厂商接口-撤销删除设备厂商

###### URL
/deviceFirms/recoverDeviceFirm
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 6. 设备厂商接口-设备厂商详细

###### URL
/deviceFirms/getDeviceFirmById
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

-------------------------- 设备厂商 --------------------------

-------------------------- 设备品牌 --------------------------

### 1. 设备品牌接口-查询所有品牌（列表）

###### URL
/deviceBrands/selectDeviceBrandsListPage
###### API Type
POST
###### params
{
    brandName : ""	        // 品牌名称 String  
    firmName : ""	        // 厂商名称 String  
    pageSize : ""
	currentsPage : ""

###### return
{
    
}

### 2. 设备品牌接口-添加设备品牌

###### URL
/deviceBrands/insertDeviceBrand
###### API Type
POST
###### params
{
    deviceFirmid : ""	    // 厂商ID  
    brandName : ""	        // 品牌名称  
    brandDesc : ""	        // 品牌描述 
}
###### return
{
    
}

### 3. 设备品牌接口-修改设备品牌

###### URL
/deviceBrands/updateDeviceBrand
###### API Type
POST
###### params
{
	id : ""
    deviceFirmid : ""	    // 厂商ID  
    brandName : ""	        // 品牌名称  
    brandDesc : ""	        // 品牌描述 
}
###### return
{
    
}

### 4. 设备品牌接口-删除设备品牌

###### URL
/deviceBrands/deleteDeviceBrand
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 5. 设备品牌接口-撤销删除设备品牌

###### URL
/deviceBrands/recoverDeviceBrand
###### API Type
POST
###### params
{
    id : ""  
}
###### return
{
    
}

### 6. 设备品牌接口-设备品牌详细

###### URL
/deviceBrands/getDeviceBrandById
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

-------------------------- 设备品牌 --------------------------

-------------------------- 设备 --------------------------

### 1. 设备接口-查询所有设备（列表）

###### URL
/deviceInfo/selectDeviceInfoListPage
###### API Type
POST
###### params
{
    deviceName : ""	        // 设备名称 String  
    deviceType : ""	        // 设备类型 String  
    firmName : ""	        // 厂商
	brandName : ""	        // 品牌
	isPutaway : ""	        // 是否上架 T：已上架  F：未上架
	isHired : ""	        // 是否租赁 T：已租赁  F：未租赁
	pageSize : ""
	currentsPage : ""
}
###### return
{
    
}

### 2. 设备接口-添加设备

###### URL
/deviceInfo/insertDeviceInfo
###### API Type
POST
###### params
{
    deviceCode : ""	        	// 设备号  
    deviceName : ""	        	// 设备名称  
    devicePicUrl : ""	        // 设备二维码图片地址  
	deviceLocation : ""	        // 设备投放地址  
	firmId : ""	        		// 厂商ID
	brandId : ""	        	// 设备品牌ID
	isPutaway : ""	        	// 设备是否上架 T：已上架  F：未上架  
	isHired : ""	        	// 设备是否租赁 T：已租赁  F：未租赁  
	deviceDesc : ""	        	// 设备描述  
	deviceStock : ""	        // 设备库存  
	deviceRental : ""	        // 设备租金  
	deviceDeposit : ""	        // 设备最低押金  
	deviceType : ""	        	// 设备类型  
	deviceSpec : ""	        	// 设备规格
}
###### return
{
    
}

### 3. 设备接口-修改设备

###### URL
/deviceInfo/updateDeviceInfo
###### API Type
POST
###### params
{
	id : ""
    deviceCode : ""	        	// 设备号  
    deviceName : ""	        	// 设备名称  
    devicePicUrl : ""	        // 设备二维码图片地址  
	deviceLocation : ""	        // 设备投放地址  
	firmId : ""	        		// 厂商ID  
	brandId : ""	        	// 设备品牌ID  
	isPutaway : ""	        	// 设备是否上架 T：已上架  F：未上架  
	isHired : ""	        	// 设备是否租赁 T：已租赁  F：未租赁  
	deviceDesc : ""	        	// 设备描述  
	deviceStock : ""	        // 设备库存  
	deviceRental : ""	        // 设备租金  
	deviceDeposit : ""	        // 设备最低押金  
	deviceType : ""	        	// 设备类型  
	deviceSpec : ""	        	// 设备规格
}
###### return
{
    
}

### 4. 设备接口-删除设备

###### URL
/deviceInfo/deleteDeviceInfo
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 5. 设备接口-撤销删除设备

###### URL
/deviceInfo/recoverDeviceInfo
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 6. 设备接口-设备详细

###### URL
/deviceInfo/getDeviceInfoById
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

-------------------------- 设备 --------------------------

-------------------------- 设备类型 --------------------------

### 1. 设备类型接口-查询所有设备类型（列表）

###### URL
/deviceType/getAllDeviceType
###### API Type
POST
###### params
{
     : ""	        // String  
     : ""	        // String  
     : ""	        // String  
}
###### return
{
    
}

### 2. 设备类型接口-添加设备类型

###### URL
/deviceType/insertDeviceType
###### API Type
POST
###### params
{
    deviceTypeName : ""	        // 设备类型名称  
    deviceTypeDesc : ""	        // 设备类型描述  
}
###### return
{
    
}

### 3. 设备类型接口-修改设备类型

###### URL
/deviceType/updateDeviceType
###### API Type
POST
###### params
{
	id : ""
    deviceTypeName : ""	        // 设备类型名称  
    deviceTypeDesc : ""	        // 设备类型描述  
}
###### return
{
    
}

### 4. 设备类型接口-删除设备类型

###### URL
/deviceType/deleteDeviceType
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 5. 设备类型接口-撤销删除设备类型

###### URL
/deviceType/recoverDeviceType
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

### 6. 设备类型接口-设备类型详细

###### URL
/deviceType/getDeviceTypeById
###### API Type
POST
###### params
{
    id : ""
}
###### return
{
    
}

-------------------------- 设备类型 --------------------------
##############################  设备管理API  END  ##############################