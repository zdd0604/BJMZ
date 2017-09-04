package com.hjnerp.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.hjnerp.common.EapApplication;
import com.hjnerp.dao.QiXinBaseDao;
import com.hjnerp.model.ChatHisBean;
import com.hjnerp.model.DeptInfo;
import com.hjnerp.model.FriendInfo;
import com.hjnerp.model.GroupInfo;
import com.hjnerp.model.VerfifyFriendInfo;
import com.hjnerp.net.ChatConstants;
import com.hjnerp.net.ChatPacketHelper;
import com.hjnerp.net.IQ;
import com.hjnerp.net.Msg;
import com.hjnerp.util.SharePreferenceUtil;

public class ContactBusiness
{
	public static final ArrayList<DeptInfo> getDepInfos()
	{
		ArrayList<FriendInfo> fis = QiXinBaseDao.queryFriendInfos("Y");
		HashMap<String, ArrayList<FriendInfo>> map = new HashMap<String, ArrayList<FriendInfo>>();
		for(int i = 0; i != fis.size(); ++i)
		{
			FriendInfo fi = fis.get(i);
			String dept = fi.getDeptid() + "-" + fi.getDeptname();
			ArrayList<FriendInfo> list = map.get(dept);
			if(list == null)
			{
				list = new ArrayList<FriendInfo>();
				map.put(dept, list);
			}
			list.add(fi);
		}
		ArrayList<DeptInfo> dis = new ArrayList<DeptInfo>();
		for(String str : map.keySet())
		{
			DeptInfo di = new DeptInfo();
			String[] strs = str.split("-");
			di.setDeptId(strs[0]);
			di.setDeptName(strs[1]);
			ArrayList<FriendInfo> fix = map.get(str);
			di.setChildCount(fix.size());
			di.setDeptChild(fix);
			dis.add(di);
		}
		return dis;
	}
	
	public static final ArrayList<DeptInfo> getDepInfos(String id,String[] fList)
	{
		ArrayList<FriendInfo> friendList = QiXinBaseDao.queryFriendInfos("Y");
		for (int i = 0; i < friendList.size(); i++) {
			if (id.equals(friendList.get(i).getFriendid())) {
				friendList.remove(i);
			}
		}
		if(fList != null){
		for (int i = 0; i < fList.length; i++) {
			for (int j = 0; j < friendList.size(); j++) {
				if (fList[i].equals(friendList.get(j).getFriendid())) {
					friendList.remove(j);
				}
			}
		}
		}
		HashMap<String, ArrayList<FriendInfo>> map = new HashMap<String, ArrayList<FriendInfo>>();
		for(int i = 0; i != friendList.size(); ++i)
		{
			FriendInfo fi = friendList.get(i);
			String dept = fi.getDeptid() + "-" + fi.getDeptname();
			ArrayList<FriendInfo> list = map.get(dept);
			if(list == null)
			{
				list = new ArrayList<FriendInfo>();
				map.put(dept, list);
			}
			list.add(fi);
		}
		ArrayList<DeptInfo> dis = new ArrayList<DeptInfo>();
		for(String str : map.keySet())
		{
			DeptInfo di = new DeptInfo();
			String[] strs = str.split("-");
			di.setDeptId(strs[0]);
			di.setDeptName(strs[1]);
			ArrayList<FriendInfo> fix = map.get(str);
			di.setChildCount(fix.size());
			di.setDeptChild(fix);
			dis.add(di);
		}
		return dis;
	}
	/*从联系人表获得一条信息*/
	public static  FriendInfo queryUserInfo(String id_user){
		FriendInfo friendInfo = new FriendInfo();
		ArrayList<FriendInfo> infolist = QiXinBaseDao.queryFriendInfos("YN");
		for(int i = 0;i < infolist.size();i ++){
			if( ( infolist.get(i).getFriendid().trim() ).equals(id_user)){
				friendInfo = infolist.get(i);
				return friendInfo;
			}
		}
		return friendInfo;
		
	}

	/*收到加入群通知，存储第一条群聊提示信息*/
	public static void saveBeInvitedGroup(IQ iq,boolean ifIsMYyself){
		ChatHisBean cBean = new ChatHisBean();
		cBean.setMsgTo((String) ((Map<String, Object>) iq.data)
				.get(ChatConstants.iq.DATA_KEY_GROUP_ID));
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgFrom(iq.to);
		cBean.setMsgIdRecorder( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		if(ifIsMYyself){
			cBean.setMsgcontent("你被邀请加入群");
		}else{
			cBean.setMsgcontent("群内增加新成员");
		}
		cBean.setMsgIdType(ChatConstants.msg.TYPE_ID_IQ);
		cBean.setMsgTime(String.valueOf(System.currentTimeMillis()));
		cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);

		QiXinBaseDao.insertMsg(cBean);
	}
	/*收到群通知，群内有成员退出存储第一条群聊提示信息*/
	public static void saveBeDeleteGroup(IQ iq,boolean ifIsMYyself,String[] ids){
		ChatHisBean cBean = new ChatHisBean();
		cBean.setMsgTo((String) ((Map<String, Object>) iq.data)
				.get(ChatConstants.iq.DATA_KEY_GROUP_ID));
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgFrom(iq.to);
		if(ifIsMYyself){ 
			cBean.setMsgcontent("你被邀请加入群");
		}else{
			String temps = new String();
			for(int i =0;i<ids.length;i++){
				FriendInfo friendInfo=  QiXinBaseDao.queryFriendInfoall( ids[i]);
				if(friendInfo == null){
					temps = "有人  ";
				}else{
					temps =  friendInfo.getFriendname() +","+temps;
				}
			} 
			cBean.setMsgcontent(temps + "退出群");
		}
	 
		cBean.setMsgIdType(ChatConstants.msg.TYPE_ID_IQ);
		cBean.setMsgTime(String.valueOf(System.currentTimeMillis()));
		cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);

		QiXinBaseDao.insertMsg(cBean);
	}
	
	/*存储一条群聊信息*/
	public static int saveGroupChatMsgIndb(Msg mMsg,String msgIdType) {
		ChatHisBean cBean = new ChatHisBean();
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgTo(mMsg.group);
		cBean.setMsgFrom(mMsg.from);
		cBean.setMsgIdRecorder( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
//		cBean.setMsgGroup(mMsg.group);
		cBean.setMsgcontent(mMsg.body);
		cBean.setMsgTime(String.valueOf(ChatPacketHelper
				.toMillsec(mMsg.time)));
		cBean.setMsgType(mMsg.type);
		cBean.setMsgIdType( msgIdType );
		cBean.setmsgIdFile(mMsg.file.id);
		cBean.setmsgTypeFile(mMsg.file.scene);
		cBean.setMsgType(ChatConstants.msg.TYPE_GROUPCHAT);
		return QiXinBaseDao.insertMsg(cBean);
	}
	/*存储一条单聊信息*/
	public static int saveSingleChatMsgIndb(Msg mMsg,String msgIdType) {
		ChatHisBean cBean = new ChatHisBean();
		cBean.setId(ChatPacketHelper.getGeneralUUID());
		cBean.setMsgTo(mMsg.to);
		cBean.setMsgFrom(mMsg.from);
		cBean.setMsgIdRecorder( SharePreferenceUtil.getInstance(EapApplication.getApplication().getApplicationContext()).getMyId());
		cBean.setMsgcontent(mMsg.body);
		cBean.setMsgTime(String.valueOf(ChatPacketHelper
				.toMillsec(mMsg.time))); 
		cBean.setMsgType(ChatConstants.msg.TYPE_CHAT);
		cBean.setMsgIdType( msgIdType );
		cBean.setmsgIdFile(mMsg.file.id);
		cBean.setmsgTypeFile(mMsg.file.scene);
		return QiXinBaseDao.insertMsg(cBean);
	}
	
	/*根据ID,改变一个联系人的标示*/
	public static void setContactFlag(String userId,char flag){
		FriendInfo userInfo = queryUserInfo(userId);
		QiXinBaseDao.replaceFriendInfo(userInfo, flag);
		
	}
	/*删除一个好友*/
	public static void deleFriendById(String myId,String friendId){
//		QiXinBaseDao.deleteContactById(friendId);
		QiXinBaseDao.updateContactAsNOFriendById(friendId);
		QiXinBaseDao.deleteSingleMsgById(myId,friendId);
	}

	/*FriendInfo和VerfifyFriendInfo转换*/
	public static VerfifyFriendInfo makeSaveDate(FriendInfo friendInfo,String whowantadd,String result){
		VerfifyFriendInfo vfriendInfo = new VerfifyFriendInfo();
		vfriendInfo.setFriendid(friendInfo.getFriendid());
		vfriendInfo.setFriendimage(friendInfo.getFriendimage());
		vfriendInfo.setFriendname(friendInfo.getFriendname());
		vfriendInfo.setDeptid(friendInfo.getDeptid());
		vfriendInfo.setDeptname(friendInfo.getDeptname());
		vfriendInfo.setFrienddescribe(friendInfo.getFrienddescribe());
		vfriendInfo.setFriendmail(friendInfo.getFriendmail());
		vfriendInfo.setFriendmtel(friendInfo.getFriendmtel());
		
		
		vfriendInfo.setVerfifyType(whowantadd);
		vfriendInfo.setVerfifyResult(result);
		
		return vfriendInfo;
	}
	/*推出群解散群*/
	public static void DropGroup(String groupId,String[] userIds){
		QiXinBaseDao.deleteGroupMsgById(groupId); //删除群聊天记录
		QiXinBaseDao.deleteGroupRelations(groupId,userIds);//删除群成员关系
		QiXinBaseDao.deleteGroupInfoById(groupId); //删除群信息
	}
	/*根据Id检查是否有此好友*/
	public static Boolean checkFriends(String friendId) {
		Boolean isExist = false;
		FriendInfo friendInfo = QiXinBaseDao.queryFriendInfo(friendId);
		if(friendInfo == null){
			isExist = false;
		}else{
			isExist = true;
		}
		
		return isExist;
	}
	/*根据电话号码检查是否有此好友*/
	public static Boolean checkFriendsByPhoneNumber(String friendPhone) {
		Boolean isExist = false;
		FriendInfo friendInfo = QiXinBaseDao.queryFriendInfoByPhoneNumber(friendPhone);
		if(friendInfo == null){
			isExist = false;
		}else{
			isExist = true;
		}
		
		return isExist;
	}
	
	
	/*判断本地是否存在此群*/
	public static Boolean checkGroup(String groupId){
		Boolean isExist = false;
		GroupInfo groupInfo = QiXinBaseDao.queryGroupInfo(groupId);
		if(groupInfo == null){
			isExist = false;
		}else{
			isExist = true;
		}
		
		return isExist;
	}
	
	
}
