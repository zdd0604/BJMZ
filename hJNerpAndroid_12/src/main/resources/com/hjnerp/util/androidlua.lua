 
-- json操作json
function hju_getjsonvalues(  jsonvalues,field) 
	local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
	local value =stc:jhju_getjsonvalue(jsonvalues,field) 
	return value
end

function hju_getuuid()

    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
	local value =stc:jhjc_getuuid() 
	return value
end 
 
 --------------------界面函数
---  取父结点
function hjv_getparentnode(viewid)
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_getparentnode(viewid)
end  
---
-- 保存一个View的数据到1347
function hjv_savedata(viewid) 
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_savedata(viewid)
end

-- 设置一个View的单据号
function hjv_setbillno(viewid, billno) 
     local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjv_setbillno(viewid,billno)
end 
--  数据库操作  
function hjdb_getvalue(billno,id_node,column)

       local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       return  stc:getdb_nodevalue(billno,id_node,column)
end
 
 --  数据库操作
function hjdb_updatevalue(billno,nodeid,field,values)
   local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:setdb_nodevalue(billno,nodeid,field,values) 
end
  
----------------数据集操作
-- 添加一项数据集
function hjdts_additem(viewid,dataset,condition) 
	  local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jdts_additem(viewid,dataset,condition) 
end

-- 修改数据集的条件
function hjdts_setcondition(viewid, key, condition)
      local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjdts_setcondition(viewid,key,condition) 
end 
    
-- 界面数据源添加
function hjds_search(viewid,datasource,condition) 
      local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjds_search(viewid,datasource,condition) 
end 
-- 设置值接口 不定的为行列 
function hjc_setvalue(viewid,controlid,values,...) 
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       if 0== arg.n then
         stc:jhjc_setvalue(viewid,controlid,values) 
       else
         stc:hjc_setvalue(viewid,controlid,arg[1],arg[2],values) 
       end 
 end   
-- 取值接口  不定的为行列 
function hjc_getvalue(viewid,controlid,... )
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       if 0== arg.n then
         return stc:jhjc_getvalue(viewid,controlid) 
       else
         return  stc:jhjc_getvalue(viewid,controlid,arg[1],arg[2]);
       end  
 end 
 function hjc_additem(viewid,controlid,billno,nodeid,value)
       local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
       stc:jhjc_additem( viewid, controlid, billno,nodeid, value)
 end 
--  取当前界面的 ParentNode 
function hjv_getbillno(viewid) 
    local stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    return  stc:jhjv_getbillno(viewid)
end 
 

-- 调用下一个界面
function hjc_setnextview(viewid,controlid, row,viewid2 ,billno,nodeid) 
   local stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
   stc:jhjc_setnextview(viewid,controlid, row,viewid2,billno,nodeid,sender.values) 
end

-- 返回前面界面
function hjc_setbackview(viewid) 
   local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:jhjc_setbackview(viewid)
end

-- 关闭界面
function hjc_setfinishview(viewid) 
   local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
   stc:jhjc_setfinishview(viewid)
end

-- 设置一个控件的隐藏和显示
function hjc_visible(viewid, controlid, visible)
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:setcontorl_visible(viewid, controlid, visible)
end

--  数据上传（由父节点开始查找数据）
function hjc_controlupload(viewid,controlid )
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_controluplod(viewid,controlid )
end

--  数据上传(有子节点开始查找数据)
function hjdb_nodeupload(billno )
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjdb_nodeuplod(billno )
end

-- 刷新控件数据源
function hjc_datarefresh(viewid,controlid,datasource)  
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_datarefresh(viewid,controlid,datasource)
end

---定位
function hjc_getlocation(viewid,controlid)  
    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
    stc:jhjc_Location(viewid, controlid) 
end  
---调用拍照
---function hjc_setphoto(viewid,controlid)
---   local levl =  context:getControl(viewid,controlid)
---   levl:setPhoto()
---end

---展示当前单据全部照片
function hjc_browsephoto(viewid,billno,nodeid)
	-- context:startShowPictures(billno,nodeid)
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhjc_browsephoto(viewid,billno,nodeid); 
end 
----控件行数
function hjc_getrowcount(viewid,controlid)
     local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     return  stc:jhjc_getrowcount(viewid,controlid);
end  
--- 拜访业务 
 function hjb_setddiscard(viewid,locationid,photoid, nextview)
    local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua") 
     stc:jhjb_setddiscard(viewid,locationid,photoid,nextview, sender.billno,sender.nodeid  )  
 end  
function hju_getuuid()
	 local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     return  stc:jhju_getuuid();
end 
-----发短信
function hju_sendsms(viewid,phone,sms)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
     stc:jhju_sendsms(viewid,phone,sms); 
end  
-----提示框
function hjc_setmakeText(viewid,maketext)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjc_setmaketext(viewid,maketext);
end  
-----设置控件是否可用
function hjc_setenabled(viewid,controlid,enabled)
      local  stc=luajava.newInstance("com.hjnerp.business.BusinessLua")
      stc:jhjc_setenabled(viewid,controlid,enabled);
end  
