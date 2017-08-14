package com.sinosafe.xb.manager.module.home.xiaofeidai.bean;

/**
 * 类名称：   com.sinosafe.xb.manager.module.home.xiaofeidai.bean
 * 内容摘要： //亚联盟银行卡校验结果bean。
 * 修改备注：
 * 创建时间： 2017/8/4 0004
 * 公司：     深圳市华移科技股份有限公司
 * 作者：     Mr 张
 */
public class YaLianMengResult {

    private String msg;
    private String result;
    private String orderId;
    private Data data;
    private String sign;
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public String getResult() {
        return result;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getSign() {
        return sign;
    }

    public static class Data {

        private String code;
        private String desc;
        private String org_code;
        private String org_desc;
        private String bank_id;
        private String bank_description;

        //银行卡信息描述
        private String card_description;//": "龙卡通",
        private String card_bin;//": "621700",
        //卡类型(1 借记卡  2 货记卡  3预付卡  4 准贷记卡)
        private String card_type;//": "1",
        //private String bank_id;//": "CCB",
        private String pay_id;//": "3003"
        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }

        public void setOrg_code(String org_code) {
            this.org_code = org_code;
        }
        public String getOrg_code() {
            return org_code;
        }

        public void setOrg_desc(String org_desc) {
            this.org_desc = org_desc;
        }
        public String getOrg_desc() {
            return org_desc;
        }

        public void setBank_id(String bank_id) {
            this.bank_id = bank_id;
        }
        public String getBank_id() {
            return bank_id;
        }

        public void setBank_description(String bank_description) {
            this.bank_description = bank_description;
        }
        public String getBank_description() {
            return bank_description;
        }

        public String getCard_description() {
            return card_description;
        }

        public void setCard_description(String card_description) {
            this.card_description = card_description;
        }

        public String getCard_bin() {
            return card_bin;
        }

        public void setCard_bin(String card_bin) {
            this.card_bin = card_bin;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }

        public String getPay_id() {
            return pay_id;
        }

        public void setPay_id(String pay_id) {
            this.pay_id = pay_id;
        }
    }
}
