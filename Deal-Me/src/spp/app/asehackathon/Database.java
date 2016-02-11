package spp.app.asehackathon;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	public static final String TABLE_PLACES = "PLACES_TABLE";
	public static final String LAT = "LAT";
	public static final String LON = "LON";
	public static final String ADDRESS = "ADDRESS";
	public static final String NAME = "NAME";
	public static final String TABLE_DEALS = "DEALS_TABLE";
	public static final String STORE_NAME = "STORE_NAME";
	public static final String DEAL_TITLE = "DEAL_TITLE";
	public static final String INFO_DEAL = "DEAL_INFO";
	public static final String STORE_ADDRESS = "STORE_ADDRESS";
	public static final String URL = "URL";
	public static final String STORE_URL = "STORE_URL";
	public static final String STORE_LAT = "STORE_LAT";
	public static final String STORE_LON = "STORE_LON";
	public static final String DEAL_EXPIRATION = "DEAL_EXPIRATION";

	public Database(Context context) {
		super(context, "deals.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql1 = String
				.format("create table %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL)",
						TABLE_PLACES,NAME, ADDRESS, LAT, LON);

		String sql2 = String
				.format("create table %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL, %s STRING NOT NULL)",
						TABLE_DEALS, STORE_NAME, DEAL_TITLE, INFO_DEAL,
						STORE_ADDRESS, URL, STORE_URL, STORE_LAT, STORE_LON, DEAL_EXPIRATION);

		db.execSQL(sql1);
		db.execSQL(sql2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void storePlaceDetails(String name, String address, String lat,
			String lon) {
		SQLiteDatabase db = getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(NAME, name);
		values.put(ADDRESS, address);
		values.put(LAT, lat);
		values.put(LON, lon);

		db.insert(TABLE_PLACES, null, values);

		db.close();
	}

	public ArrayList<PlaceDbDetails> getPlaceDetails() {
		ArrayList<PlaceDbDetails> values = new ArrayList<PlaceDbDetails>();
		SQLiteDatabase db = getWritableDatabase();

		String sql = String.format(
				"SELECT %s, %s, %s, %s FROM %s ORDER BY _id", NAME, ADDRESS,
				LAT, LON, TABLE_PLACES);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {

			String name = cursor.getString(0);
			String address = cursor.getString(1);
			double latitude = Double.parseDouble(cursor.getString(2));
			double longitude = Double.parseDouble(cursor.getString(3));
			PlaceDbDetails pDetails = new PlaceDbDetails(name, address, latitude,
					longitude);
			// Location arr = new Location(latitude, longitude);
			if (!values.contains(pDetails))
				values.add(pDetails);

		}

		db.close();
		return values;

	}

	class PlaceDbDetails {
		String pname;
		String paddress;
		double latitude;
		double longitude;

		public PlaceDbDetails(String pname, String paddress, double latitude,
				double longitude) {
			super();
			this.pname = pname;
			this.paddress = paddress;
			this.latitude = latitude;
			this.longitude = longitude;
		}

		public String getPname() {
			return pname;
		}

		public void setPname(String pname) {
			this.pname = pname;
		}

		public String getPaddress() {
			return paddress;
		}

		public void setPaddress(String paddress) {
			this.paddress = paddress;
		}

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(latitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(longitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((paddress == null) ? 0 : paddress.hashCode());
			result = prime * result + ((pname == null) ? 0 : pname.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PlaceDbDetails other = (PlaceDbDetails) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(latitude) != Double
					.doubleToLongBits(other.latitude))
				return false;
			if (Double.doubleToLongBits(longitude) != Double
					.doubleToLongBits(other.longitude))
				return false;
			if (paddress == null) {
				if (other.paddress != null)
					return false;
			} else if (!paddress.equals(other.paddress))
				return false;
			if (pname == null) {
				if (other.pname != null)
					return false;
			} else if (!pname.equals(other.pname))
				return false;
			return true;
		}

		private Database getOuterType() {
			return Database.this;
		}

	}

	public void storeDealDetails(String name, String dealTitle,
			String dealInfo, String address, String url, String storeUrl,
			String lat, String lon, String expiration) {
		SQLiteDatabase db = getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(STORE_NAME, name);
		values.put(DEAL_TITLE, dealTitle);
		values.put(INFO_DEAL, dealInfo);
		values.put(STORE_ADDRESS, address);
		values.put(URL, url);
		values.put(STORE_URL, storeUrl);
		values.put(STORE_LAT, lat);
		values.put(STORE_LON, lon);
		values.put(DEAL_EXPIRATION, expiration);

		db.insert(TABLE_DEALS, null, values);

		db.close();
	}

	public ArrayList<DealDbDetails> getDealDetails() {
		ArrayList<DealDbDetails> values = new ArrayList<DealDbDetails>();
		SQLiteDatabase db = getWritableDatabase();

		String sql = String.format(
				"SELECT %s, %s, %s, %s, %s, %s, %s, %s, %s FROM %s ORDER BY _id",
				STORE_NAME, DEAL_TITLE, INFO_DEAL, STORE_ADDRESS, URL,
				STORE_URL, STORE_LAT, STORE_LON, DEAL_EXPIRATION, TABLE_DEALS);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {

			String name = cursor.getString(0);
			String dealTitle = cursor.getString(1);
			String dealInfo = cursor.getString(2);
			String address = cursor.getString(3);
			String url = cursor.getString(4);
			String storeUrl = cursor.getString(5);
			
			double latitude = Double.parseDouble(cursor.getString(6));
			double longitude = Double.parseDouble(cursor.getString(7));
			String expiration = cursor.getString(8);
			DealDbDetails dDetails = new DealDbDetails(name, dealTitle, dealInfo,
					address, url, storeUrl, latitude, longitude, expiration);
			if (!values.contains(dDetails))
				values.add(dDetails);

		}

		db.close();
		return values;

	}

	class DealDbDetails {
		String storeName;
		String dealTitle;
		String dealInfo;
		String storeAddress;
		String url;
		String storeUrl;
		double storeLat;
		double storeLon;
		String deal_expiration;

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public String getDealTitle() {
			return dealTitle;
		}

		public void setDealTitle(String dealTitle) {
			this.dealTitle = dealTitle;
		}

		public String getDealInfo() {
			return dealInfo;
		}

		public void setDealInfo(String dealInfo) {
			this.dealInfo = dealInfo;
		}

		public String getStoreAddress() {
			return storeAddress;
		}

		public void setStoreAddress(String storeAddress) {
			this.storeAddress = storeAddress;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getStoreUrl() {
			return storeUrl;
		}

		public void setStoreUrl(String storeUrl) {
			this.storeUrl = storeUrl;
		}

		public double getStoreLat() {
			return storeLat;
		}

		public void setStoreLat(double storeLat) {
			this.storeLat = storeLat;
		}

		public double getStoreLon() {
			return storeLon;
		}

		public void setStoreLon(double storeLon) {
			this.storeLon = storeLon;
		}

		public String getDeal_expiration() {
			return deal_expiration;
		}

		public void setDeal_expiration(String deal_expiration) {
			this.deal_expiration = deal_expiration;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((dealInfo == null) ? 0 : dealInfo.hashCode());
			result = prime * result
					+ ((dealTitle == null) ? 0 : dealTitle.hashCode());
			result = prime
					* result
					+ ((deal_expiration == null) ? 0 : deal_expiration
							.hashCode());
			result = prime * result
					+ ((storeAddress == null) ? 0 : storeAddress.hashCode());
			long temp;
			temp = Double.doubleToLongBits(storeLat);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(storeLon);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((storeName == null) ? 0 : storeName.hashCode());
			result = prime * result
					+ ((storeUrl == null) ? 0 : storeUrl.hashCode());
			result = prime * result + ((url == null) ? 0 : url.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			DealDbDetails other = (DealDbDetails) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (dealInfo == null) {
				if (other.dealInfo != null)
					return false;
			} else if (!dealInfo.equals(other.dealInfo))
				return false;
			if (dealTitle == null) {
				if (other.dealTitle != null)
					return false;
			} else if (!dealTitle.equals(other.dealTitle))
				return false;
			if (deal_expiration == null) {
				if (other.deal_expiration != null)
					return false;
			} else if (!deal_expiration.equals(other.deal_expiration))
				return false;
			if (storeAddress == null) {
				if (other.storeAddress != null)
					return false;
			} else if (!storeAddress.equals(other.storeAddress))
				return false;
			if (Double.doubleToLongBits(storeLat) != Double
					.doubleToLongBits(other.storeLat))
				return false;
			if (Double.doubleToLongBits(storeLon) != Double
					.doubleToLongBits(other.storeLon))
				return false;
			if (storeName == null) {
				if (other.storeName != null)
					return false;
			} else if (!storeName.equals(other.storeName))
				return false;
			if (storeUrl == null) {
				if (other.storeUrl != null)
					return false;
			} else if (!storeUrl.equals(other.storeUrl))
				return false;
			if (url == null) {
				if (other.url != null)
					return false;
			} else if (!url.equals(other.url))
				return false;
			return true;
		}

		private Database getOuterType() {
			return Database.this;
		}

		public DealDbDetails(String storeName, String dealTitle,
				String dealInfo, String storeAddress, String url,
				String storeUrl, double storeLat, double storeLon,
				String deal_expiration) {
			super();
			this.storeName = storeName;
			this.dealTitle = dealTitle;
			this.dealInfo = dealInfo;
			this.storeAddress = storeAddress;
			this.url = url;
			this.storeUrl = storeUrl;
			this.storeLat = storeLat;
			this.storeLon = storeLon;
			this.deal_expiration = deal_expiration;
		}

	

		
	}
}
