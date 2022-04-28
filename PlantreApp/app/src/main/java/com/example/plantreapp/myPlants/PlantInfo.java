package com.example.plantreapp.myPlants;

import android.os.Parcel;
import android.os.Parcelable;

public class PlantInfo implements Parcelable {
    private String name, scifiName, uri, description, stage;
    private float seedWaterRate, seedlingWaterRate, matureWaterRate, minSeedMoisture, maxSeedMoisture,
            minSeedlingMoisture, maxSeedlingMoisture, minMatureMoisture, maxMatureMoisture;
    private int waterTime;

    public PlantInfo(String name, String scifiName, String uri, String description, String stage, float seedWaterRate,
                     float seedlingWaterRate, float matureWaterRate, float minSeedMoisture, float maxSeedMoisture,
                     float minSeedlingMoisture, float maxSeedlingMoisture, float minMatureMoisture, float maxMatureMoisture, int waterTime) {
        this.name = name;
        this.scifiName = scifiName;
        this.uri = uri;
        this.description = description;
        this.stage = stage;
        this.seedWaterRate = seedWaterRate;
        this.seedlingWaterRate = seedlingWaterRate;
        this.matureWaterRate = matureWaterRate;
        this.minSeedMoisture = minSeedMoisture;
        this.maxSeedMoisture = maxSeedMoisture;
        this.minSeedlingMoisture = minSeedlingMoisture;
        this.maxSeedlingMoisture = maxSeedlingMoisture;
        this.minMatureMoisture = minMatureMoisture;
        this.maxMatureMoisture = maxMatureMoisture;
        this.waterTime = waterTime;
    }

    protected PlantInfo(Parcel in) {
        name = in.readString();
        scifiName = in.readString();
        uri = in.readString();
        description = in.readString();
        stage = in.readString();
        seedWaterRate = in.readFloat();
        seedlingWaterRate = in.readFloat();
        matureWaterRate = in.readFloat();
        minSeedMoisture = in.readFloat();
        maxSeedMoisture = in.readFloat();
        minSeedlingMoisture = in.readFloat();
        maxSeedlingMoisture = in.readFloat();
        minMatureMoisture = in.readFloat();
        maxMatureMoisture = in.readFloat();
        waterTime = in.readInt();
    }

    public static final Creator<PlantInfo> CREATOR = new Creator<PlantInfo>() {
        @Override
        public PlantInfo createFromParcel(Parcel in) {
            return new PlantInfo(in);
        }

        @Override
        public PlantInfo[] newArray(int size) {
            return new PlantInfo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScifiName() {
        return scifiName;
    }

    public void setScifiName(String scifiName) {
        this.scifiName = scifiName;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public float getSeedWaterRate() {
        return seedWaterRate;
    }

    public void setSeedWaterRate(float seedWaterRate) {
        this.seedWaterRate = seedWaterRate;
    }

    public float getSeedlingWaterRate() {
        return seedlingWaterRate;
    }

    public void setSeedlingWaterRate(float seedlingWaterRate) {
        this.seedlingWaterRate = seedlingWaterRate;
    }

    public float getMatureWaterRate() {
        return matureWaterRate;
    }

    public void setMatureWaterRate(float matureWaterRate) {
        this.matureWaterRate = matureWaterRate;
    }

    public float getMinSeedMoisture() {
        return minSeedMoisture;
    }

    public void setMinSeedMoisture(float minSeedMoisture) {
        this.minSeedMoisture = minSeedMoisture;
    }

    public float getMaxSeedMoisture() {
        return maxSeedMoisture;
    }

    public void setMaxSeedMoisture(float maxSeedMoisture) {
        this.maxSeedMoisture = maxSeedMoisture;
    }

    public float getMinSeedlingMoisture() {
        return minSeedlingMoisture;
    }

    public void setMinSeedlingMoisture(float minSeedlingMoisture) {
        this.minSeedlingMoisture = minSeedlingMoisture;
    }

    public float getMaxSeedlingMoisture() {
        return maxSeedlingMoisture;
    }

    public void setMaxSeedlingMoisture(float maxSeedlingMoisture) {
        this.maxSeedlingMoisture = maxSeedlingMoisture;
    }

    public float getMinMatureMoisture() {
        return minMatureMoisture;
    }

    public void setMinMatureMoisture(float minMatureMoisture) {
        this.minMatureMoisture = minMatureMoisture;
    }

    public float getMaxMatureMoisture() {
        return maxMatureMoisture;
    }

    public void setMaxMatureMoisture(float maxMatureMoisture) {
        this.maxMatureMoisture = maxMatureMoisture;
    }

    public int getWaterTime() {
        return waterTime;
    }

    public void setWaterTime(int waterTime) {
        this.waterTime = waterTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(scifiName);
        parcel.writeString(uri);
        parcel.writeString(description);
        parcel.writeString(stage);
        parcel.writeFloat(seedWaterRate);
        parcel.writeFloat(seedlingWaterRate);
        parcel.writeFloat(matureWaterRate);
        parcel.writeFloat(minSeedMoisture);
        parcel.writeFloat(maxSeedMoisture);
        parcel.writeFloat(minSeedlingMoisture);
        parcel.writeFloat(maxSeedlingMoisture);
        parcel.writeFloat(minMatureMoisture);
        parcel.writeFloat(maxMatureMoisture);
        parcel.writeInt(waterTime);

    }
}
