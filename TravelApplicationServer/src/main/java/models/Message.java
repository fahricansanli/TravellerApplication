package models;

import util.GsonBuilderHelper;

public class Message {
    private Type type;
    private String jsonData;
    private String optionalSecondData;
    private String optionalThirdData;
    private String optionalFourthData;

    public Message(Type type, String data, String optionalSecondData, String optionalThirdData, String optionalFourthData) {
        this.type = type;
        this.jsonData = data;
        this.optionalSecondData = optionalSecondData;
        this.optionalThirdData  = optionalThirdData;
        this.optionalFourthData  = optionalFourthData;
    }

    public Type getType() {
        return type;
    }

    public String getJsonData() {
        return jsonData;
    }

    public String getOptionalSecondData() {
        return optionalSecondData;
    }

    public String getOptionalThirdData() {
        return optionalThirdData;
    }

    public String getOptionalFourthData() {
        return optionalFourthData;
    }

    public <T> T convertToObject(Class<T> className){
        return GsonBuilderHelper.getJsonBuilder().fromJson(this.jsonData,className);
    }
}
