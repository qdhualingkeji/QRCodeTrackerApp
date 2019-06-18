package com.hualing.qrcodetracker.dao;

import com.hualing.qrcodetracker.aframework.yoni.ActionRequest;
import com.hualing.qrcodetracker.aframework.yoni.ActionResult;
import com.hualing.qrcodetracker.bean.BCPCKDResult;
import com.hualing.qrcodetracker.bean.BCPINParam;
import com.hualing.qrcodetracker.bean.BCPOutShowDataResult;
import com.hualing.qrcodetracker.bean.BCPRKDResult;
import com.hualing.qrcodetracker.bean.BCPTKDResult;
import com.hualing.qrcodetracker.bean.BCPTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.BCPTKParam;
import com.hualing.qrcodetracker.bean.BCPTKShowDataResult;
import com.hualing.qrcodetracker.bean.BcpInQualityCheckResult;
import com.hualing.qrcodetracker.bean.BcpInShowBean;
import com.hualing.qrcodetracker.bean.BcpInVerifyResult;
import com.hualing.qrcodetracker.bean.BcpOutGetShowDataParam;
import com.hualing.qrcodetracker.bean.BcpOutParam;
import com.hualing.qrcodetracker.bean.BcpOutVerifyResult;
import com.hualing.qrcodetracker.bean.BcpThrowGetShowDataParam;
import com.hualing.qrcodetracker.bean.BcpThrowParam;
import com.hualing.qrcodetracker.bean.BcpThrowShowDataResult;
import com.hualing.qrcodetracker.bean.BcpTkQualityCheckResult;
import com.hualing.qrcodetracker.bean.BcpTkVerifyResult;
import com.hualing.qrcodetracker.bean.BcpTrackResult;
import com.hualing.qrcodetracker.bean.BigCPINParam;
import com.hualing.qrcodetracker.bean.BigCpOutGetDataParam;
import com.hualing.qrcodetracker.bean.BigCpOutGetDataResult;
import com.hualing.qrcodetracker.bean.BigCpOutParam;
import com.hualing.qrcodetracker.bean.BigCpResult;
import com.hualing.qrcodetracker.bean.BigCpTrackResult;
import com.hualing.qrcodetracker.bean.CJResult;
import com.hualing.qrcodetracker.bean.CheckExistParam;
import com.hualing.qrcodetracker.bean.CpOutVerifyResult;
import com.hualing.qrcodetracker.bean.CreateBCPCKDParam;
import com.hualing.qrcodetracker.bean.CreateBCPRKDParam;
import com.hualing.qrcodetracker.bean.CreateBCPTKDParam;
import com.hualing.qrcodetracker.bean.CreateWLCKDParam;
import com.hualing.qrcodetracker.bean.CreateWLRKDParam;
import com.hualing.qrcodetracker.bean.CreateWLTKDParam;
import com.hualing.qrcodetracker.bean.DataBean;
import com.hualing.qrcodetracker.bean.DataInputParams;
import com.hualing.qrcodetracker.bean.DataResult;
import com.hualing.qrcodetracker.bean.GXResult;
import com.hualing.qrcodetracker.bean.GetGXParam;
import com.hualing.qrcodetracker.bean.GetNeedInputedDataParams;
import com.hualing.qrcodetracker.bean.GetSXYLParam;
import com.hualing.qrcodetracker.bean.HlProductParam;
import com.hualing.qrcodetracker.bean.HlProductResult;
import com.hualing.qrcodetracker.bean.HlSortResult;
import com.hualing.qrcodetracker.bean.LoginParams;
import com.hualing.qrcodetracker.bean.LoginResult;
import com.hualing.qrcodetracker.bean.MainParams;
import com.hualing.qrcodetracker.bean.MainResult;
import com.hualing.qrcodetracker.bean.MaterialInParams;
import com.hualing.qrcodetracker.bean.MaterialOutParams;
import com.hualing.qrcodetracker.bean.Module2Result;
import com.hualing.qrcodetracker.bean.NonCheckResult;
import com.hualing.qrcodetracker.bean.NotificationParam;
import com.hualing.qrcodetracker.bean.PdtSortResult;
import com.hualing.qrcodetracker.bean.PersonBean;
import com.hualing.qrcodetracker.bean.PersonParam;
import com.hualing.qrcodetracker.bean.PersonResult;
import com.hualing.qrcodetracker.bean.ProductInParams;
import com.hualing.qrcodetracker.bean.ProductOutParams;
import com.hualing.qrcodetracker.bean.QualityDataParam;
import com.hualing.qrcodetracker.bean.QualityDataResult;
import com.hualing.qrcodetracker.bean.SXYLResult;
import com.hualing.qrcodetracker.bean.SmallCPINParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataParam;
import com.hualing.qrcodetracker.bean.SmallCpOutGetDataResult;
import com.hualing.qrcodetracker.bean.SmallCpOutParam;
import com.hualing.qrcodetracker.bean.SmallCpTrackResult;
import com.hualing.qrcodetracker.bean.UserGroupResult;
import com.hualing.qrcodetracker.bean.VerifyParam;
import com.hualing.qrcodetracker.bean.WLCKDResult;
import com.hualing.qrcodetracker.bean.WLINParam;
import com.hualing.qrcodetracker.bean.WLOutGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLOutParam;
import com.hualing.qrcodetracker.bean.WLOutShowDataResult;
import com.hualing.qrcodetracker.bean.WLRKDResult;
import com.hualing.qrcodetracker.bean.WLTKDResult;
import com.hualing.qrcodetracker.bean.WLTKGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLTKParam;
import com.hualing.qrcodetracker.bean.WLTKShowDataResult;
import com.hualing.qrcodetracker.bean.WLThrowGetShowDataParam;
import com.hualing.qrcodetracker.bean.WLThrowParam;
import com.hualing.qrcodetracker.bean.WLThrowShowDataResult;
import com.hualing.qrcodetracker.bean.WlInQualityCheckResult;
import com.hualing.qrcodetracker.bean.WlInVerifyResult;
import com.hualing.qrcodetracker.bean.WlOutVerifyResult;
import com.hualing.qrcodetracker.bean.WlTkQualityCheckResult;
import com.hualing.qrcodetracker.bean.WlTkVerifyResult;
import com.hualing.qrcodetracker.bean.WlTrackParam;
import com.hualing.qrcodetracker.bean.WlTrackResult;
import com.hualing.qrcodetracker.global.GlobalData;
import com.hualing.qrcodetracker.model.User;

import org.json.JSONObject;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public interface MainDao {

    @ActionRequest(func = GlobalData.Service.LOGIN)
    ActionResult<LoginResult> login(LoginParams params);

    @ActionRequest(func = GlobalData.Service.GET_INPUTED_DATA)
    ActionResult<DataResult> getNeedInputedData(GetNeedInputedDataParams params);

    @ActionRequest(func = GlobalData.Service.COMMIT_INPUTED_DATA)
    ActionResult<ActionResult> commitInputedData(DataInputParams params);

    @ActionRequest(func = GlobalData.Service.MATERAIL_IN)
    ActionResult<ActionResult> materialIn(MaterialInParams params);

    @ActionRequest(func = GlobalData.Service.MATERAIL_OUT)
    ActionResult<ActionResult> materialOut(MaterialOutParams params);

    @ActionRequest(func = GlobalData.Service.PRODUCT_IN)
    ActionResult<ActionResult> productIn(ProductInParams params);

    @ActionRequest(func = GlobalData.Service.PRODUCT_OUT)
    ActionResult<ActionResult> productOut(ProductOutParams params);

    @ActionRequest(func = GlobalData.Service.GET_MAIN_DATA)
    ActionResult<MainResult> getMainData(MainParams params);

    @ActionRequest(func = GlobalData.Service.COMMIT_MATERIALIN_INPUTED_DATA)
    ActionResult<ActionResult> commitMaterialInInputedData(WLINParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_WL_RKD)
    ActionResult<WLRKDResult> createWL_RKD(CreateWLRKDParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_WL_CKD)
    ActionResult<WLCKDResult> createWL_CKD(CreateWLCKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_PDT_SORT)
    ActionResult<PdtSortResult> getPdtSort();

    @ActionRequest(func = GlobalData.Service.GET_HL_SORT)
    ActionResult<HlSortResult> getHlSort();

    @ActionRequest(func = GlobalData.Service.GET_HL_PRODUCT)
    ActionResult<HlProductResult> getHlProduct(HlProductParam param);

    @ActionRequest(func = GlobalData.Service.GET_WL_OUT_SHOW_DATA)
    ActionResult<WLOutShowDataResult> getWlOutShowData(WLOutGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.GET_BCP_OUT_SHOW_DATA)
    ActionResult<BCPOutShowDataResult> getBcpOutShowData(BcpOutGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_OUT)
    ActionResult<ActionResult> wlOut(WLOutParam params);

    @ActionRequest(func = GlobalData.Service.BCP_OUT)
    ActionResult<ActionResult> bcpOut(BcpOutParam params);

    @ActionRequest(func = GlobalData.Service.GET_DEPARTMENT_DATA)
    ActionResult<UserGroupResult> getDepartmentData();

    @ActionRequest(func = GlobalData.Service.CREATE_WL_TKD)
    ActionResult<WLTKDResult> createWL_TKD(CreateWLTKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_WL_TK_SHOW_DATA)
    ActionResult<WLTKShowDataResult> getWlTKShowData(WLTKGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_TK)
    ActionResult<ActionResult> wlTK(WLTKParam params);

    @ActionRequest(func = GlobalData.Service.GET_WL_THROW_SHOW_DATA)
    ActionResult<WLThrowShowDataResult> getWlThrowShowData(WLThrowGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.WL_THROW)
    ActionResult<ActionResult> wlThrow(WLThrowParam params);

    @ActionRequest(func = GlobalData.Service.GET_GX)
    ActionResult<GXResult> getGX(GetGXParam param);

    @ActionRequest(func = GlobalData.Service.GET_CJ)
    ActionResult<CJResult> getCJData();

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_RKD)
    ActionResult<BCPRKDResult> createBCP_RKD(CreateBCPRKDParam params);

    @ActionRequest(func = GlobalData.Service.BCP_IN)
    ActionResult<ActionResult> commitBCPInInputedData(BCPINParam params);

    @ActionRequest(func = GlobalData.Service.GET_TLYL)
    ActionResult<SXYLResult> getSXYL(GetSXYLParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCP_THROW_SHOW_DATA)
    ActionResult<BcpThrowShowDataResult> getBcpThrowShowData(BcpThrowGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.BCP_THROW)
    ActionResult<ActionResult> bcpThrow(BcpThrowParam params);

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_TKD)
    ActionResult<BCPTKDResult> createBCP_TKD(CreateBCPTKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_BCP_TK_SHOW_DATA)
    ActionResult<BCPTKShowDataResult> getBCPTKShowData(BCPTKGetShowDataParam getParam);

    @ActionRequest(func = GlobalData.Service.BCP_TK)
    ActionResult<ActionResult> bcpTK(BCPTKParam param);

    @ActionRequest(func = GlobalData.Service.CREATE_BCP_CKD)
    ActionResult<BCPCKDResult> createBCP_CKD(CreateBCPCKDParam params);

    @ActionRequest(func = GlobalData.Service.GET_LEI_BIE)
    ActionResult<PdtSortResult> getLeiBie();

    @ActionRequest(func = GlobalData.Service.BIG_CP_IN)
    ActionResult<ActionResult> commitBigCPInInputedData(BigCPINParam params);

    @ActionRequest(func = GlobalData.Service.SMALL_CP_IN)
    ActionResult<ActionResult> commitSmallCPInInputedData(SmallCPINParam params);

    @ActionRequest(func = GlobalData.Service.GET_BIG_CP_DATA)
    ActionResult<BigCpResult> getBigCpData();

    @ActionRequest(func = GlobalData.Service.BIG_CP_OUT)
    ActionResult<ActionResult> bigCpOut(BigCpOutParam params);

    @ActionRequest(func = GlobalData.Service.GET_BIG_CP_OUT_DATA)
    ActionResult<BigCpOutGetDataResult> getBigCpOutData(BigCpOutGetDataParam bigCpOutGetDataParam);

    @ActionRequest(func = GlobalData.Service.GET_SMALL_CP_OUT_DATA)
    ActionResult<SmallCpOutGetDataResult> getSmallCpOutData(SmallCpOutGetDataParam smallCpOutGetDataParam);

    @ActionRequest(func = GlobalData.Service.SMALL_CP_OUT)
    ActionResult<ActionResult> smallCpOut(SmallCpOutParam params);

    @ActionRequest(func = GlobalData.Service.WL_TRACK)
    ActionResult<WlTrackResult> getWlTrackShowData(WlTrackParam param);

    @ActionRequest(func = GlobalData.Service.BCP_TRACK)
    ActionResult<BcpTrackResult> getBcpTrackShowData(WlTrackParam param);

    @ActionRequest(func = GlobalData.Service.SMALL_CP_TRACK)
    ActionResult<SmallCpTrackResult> getSmallCpTrackShowData(WlTrackParam param);

    @ActionRequest(func = GlobalData.Service.BIG_CP_TRACK)
    ActionResult<BigCpTrackResult> getBigCpTrackShowData(WlTrackParam param);

    @ActionRequest(func = GlobalData.Service.SEND_NOTIFICATION)
    ActionResult<ActionResult> sendNotification(NotificationParam notificationParam);

    @ActionRequest(func = GlobalData.Service.GET_NON_CHECK_DATA)
    ActionResult<NonCheckResult> getNonCheckData(MainParams params);

    @ActionRequest(func = GlobalData.Service.GET_WLIN_VERIFY_DATA)
    ActionResult<WlInVerifyResult> getWlInVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_WLIN_QUALITY_CHECK_DATA)
    ActionResult<WlInQualityCheckResult> getWlInQualityCheckData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_WLOUT_VERIFY_DATA)
    ActionResult<WlOutVerifyResult> getWlOutVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_WLTK_VERIFY_DATA)
    ActionResult<WlTkVerifyResult> getWlTkVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_WLTK_QUALITY_CHECK_DATA)
    ActionResult<WlTkQualityCheckResult> getWlTkQualityCheckData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCPIN_VERIFY_DATA)
    ActionResult<BcpInVerifyResult> getBcpInVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_SMALL_CP_IN_VERIFY_DATA)
    ActionResult<BcpInVerifyResult> getSmallCPInVerifyData(BcpInShowBean params);

    @ActionRequest(func = GlobalData.Service.GET_BCPIN_QUALITY_CHECK_DATA)
    ActionResult<BcpInQualityCheckResult> getBcpInQualityCheckData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCPOUT_VERIFY_DATA)
    ActionResult<BcpOutVerifyResult> getBcpOutVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_CPOUT_VERIFY_DATA)
    ActionResult<CpOutVerifyResult> getCpOutVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCPTK_VERIFY_DATA)
    ActionResult<BcpTkVerifyResult> getBcpTkVerifyData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_BCPTK_QUALITY_CHECK_DATA)
    ActionResult<BcpTkQualityCheckResult> getBcpTkQualityCheckData(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_WLIN)
    ActionResult<ActionResult> toAgreeWlIn(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_WLIN)
    ActionResult<ActionResult> toRefuseWlIn(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_WLOUT)
    ActionResult<ActionResult> toRefuseWlOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_WLOUT)
    ActionResult<ActionResult> toAgreeWlOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_WLTK)
    ActionResult<ActionResult> toRefuseWlTk(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_WLTK)
    ActionResult<ActionResult> toAgreeWlTk(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_BCP_IN)
    ActionResult<ActionResult> toAgreeBcpIn(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_BCP_IN)
    ActionResult<ActionResult> toRefuseBcpIn(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_BCP_OUT)
    ActionResult<ActionResult> toRefuseBcpOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_CP_OUT)
    ActionResult<ActionResult> toRefuseCpOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_BCP_OUT)
    ActionResult<ActionResult> toAgreeBcpOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_CP_OUT)
    ActionResult<ActionResult> toAgreeCpOut(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.AGREE_BCP_TK)
    ActionResult<ActionResult> toAgreeBcpTk(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.REFUSE_BCP_TK)
    ActionResult<ActionResult> toRefuseBcpTk(VerifyParam param);

    @ActionRequest(func = GlobalData.Service.GET_QUALITY_DATA)
    ActionResult<QualityDataResult> getQualityData(QualityDataParam param);

    @ActionRequest(func = GlobalData.Service.PASS_CHECK)
    ActionResult<ActionResult> passCheck(QualityDataParam param);

    @ActionRequest(func = GlobalData.Service.UPDATA_WLIN_DATA)
    ActionResult<ActionResult> toUpdateWLInData(WlInVerifyResult param);

    @ActionRequest(func = GlobalData.Service.UPDATA_WLOUT_DATA)
    ActionResult<ActionResult> toUpdateWLOutData(WlOutVerifyResult updatedParam);

    @ActionRequest(func = GlobalData.Service.UPDATA_WLTK_DATA)
    ActionResult<ActionResult> toUpdateWLTkData(WlTkVerifyResult updatedParam);

    @ActionRequest(func = GlobalData.Service.UPDATA_BCPIN_DATA)
    ActionResult<ActionResult> toUpdateBcpInData(BcpInVerifyResult updatedParam);

    @ActionRequest(func = GlobalData.Service.UPDATA_BCPOUT_DATA)
    ActionResult<ActionResult> toUpdateBcpOutData(BcpOutVerifyResult updatedBcpParam);

    @ActionRequest(func = GlobalData.Service.UPDATA_CPOUT_DATA)
    ActionResult<ActionResult> toUpdateCpOutData(CpOutVerifyResult updatedParam);

    @ActionRequest(func = GlobalData.Service.UPDATA_BCPTK_DATA)
    ActionResult<ActionResult> toUpdateBcpTkData(BcpTkVerifyResult updatedParam);

    @ActionRequest(func = GlobalData.Service.GET_PERSON_INFO)
    ActionResult<PersonResult> getAllPerson(User userParam);

    @ActionRequest(func = GlobalData.Service.GET_PERSON_BY_ID)
    ActionResult<PersonResult> getPersonById(User param);

    @ActionRequest(func = GlobalData.Service.SEARCH_ALL_PERSON)
    ActionResult<PersonResult> searchAllPerson();

    @ActionRequest(func = GlobalData.Service.GET_CAN_MODIFY_DATA)
    ActionResult<NonCheckResult> getCanModifyData(MainParams params);

    @ActionRequest(func = GlobalData.Service.GET_SMALLCP_IN_QUALITY_CHECKDATA)
    ActionResult<BcpInQualityCheckResult> getSmallCPInQualityCheckData(BcpInShowBean params);

    @ActionRequest(func = GlobalData.Service.GET_XZQX)
    ActionResult<Module2Result> getXZQX(PersonParam param);

    @ActionRequest(func = GlobalData.Service.COMMIT_USER_REGISTERED_DATA)
    ActionResult<ActionResult> commitUserRegisteredData(PersonParam params);

    @ActionRequest(func = GlobalData.Service.UPDATE_USER_DATA)
    ActionResult<ActionResult> updateUserData(PersonResult updatedParam);

    @ActionRequest(func = GlobalData.Service.DELETE_USER)
    ActionResult<ActionResult> deleteUser(PersonParam personParam);

    @ActionRequest(func = GlobalData.Service.CHECK_EXIST_BY_QRCODEID)
    ActionResult<ActionResult> checkExistByQrCodeId(CheckExistParam param);

}
