package rp3.auna.util.constants;

/**
 * Created by Jesus Villa on 04/08/2015.
 */
public class ConstantsServices {

    public static final String URL_BASE = "https://platanitos.com/api";
    //public static final String URL_BASE = "http://dev.platanitos.com/api";
    public static final String LOGIN="PersonaAPI";
    public static final String SERVICE_SINCRONIZACION="SincronizarAPI";
    public static final String URDERLINE="/";
    public static final String LIMITED_ACCESS_API_KEY = "07FVgRo7fPgxNqSu3u2u4QjnRlf95a5oUVudQYG2";

    public static final String URL_IMAGES ="https://d13xymm0hzzbsd.cloudfront.net/";
    public static final String URL_IMAGES_LUCKY ="http://peruzamora.com/villa/images/";
    public static final String KEY_SERVICE  = "?action=";


    public static final String REST_API_LOGIN= "usuario/login";
    public static final String REST_API_NEW_USER= "set_register";
    public static final String REST_API_LOGIN_FACE= "login_face";
    public static final String REST_API_POSTS_USER= "posts_user";
    public static final String REST_API_SET_LOGIN_FACEBOOK= "set_login";

    public static final String REST_API_CATALOG= "get_items_temps_19";
    public static final String REST_API_ITEM_DETAIL= "get_items_temps_15";
    public static final String REST_API_CART_ITEMS= "get_carts_mobile";
    public static final String REST_API_SET_TO_CART= "set_carts";
    public static final String REST_API_SET_CART_DISCOUNT= "set_discounts_1";
    public static final String REST_API_GET_STORES_PASARELA= "get_stores_2";
    public static final String REST_API_GET_UGIGEOS= "get_ubigeos_2";
    public static final String REST_API_GET_USER_ADDRESSES= "get_users_addresses_2";
    public static final String REST_API_GET_UBIDEO_STORES= "get_stores_3";
    public static final String REST_API_GET_USER_PHONES= "get_users_phones";
    public static final String REST_API_GET_SHIPPING= "get_shippings_2";
    public static final String REST_API_SET_ORDER_ANDROID= "set_orders_android";
    public static final String REST_API_GET_OFFERS_ANDROID= "get_offers_android";
    public static final String REST_API_GET_ITEMS_FILTERS= "get_items_filters";
    public static final String REST_API_SET_ORDER = "set_orders_1_android";
    public static final String REST_API_GET_BANNERS = "get_taxonomies_terms_banners";
    public static final String REST_API_GET_ORDER_SUMMARY = "get_orders_summary";
    public static final String REST_API_POST_RESPONSE_VISIT_FAILED="Hubo un problema al insertar la visita";





    public static final String GENERAL_TAG_HEAD_API_KEY = "x-api-key";

    public static final String ub  = "apps@platanitos.com";
    public static final String pb  = "MV2]EGW~m*2-[+jjB{lG";

    public static final String GENERAL_OUT_TAG_ERROR_CODE = "errorCode";
    public static final String GENERAL_OUT_TAG_ERROR_DESCRIPTION = "errorDescription";





    /**/
    /*API new_faceuser_post */
    // INPUT, OUTPUT, error
    public static final String A001_INP_EMAIL = "email";
    public static final String A001_INP_NAME = "name";
    public static final String A001_INP_FACEBOOKID = "facebookId";

    public static final String A001_OUT_EMAIL = "email";
    public static final String A001_OUT_NAME = "name";
    public static final String A001_OUT_FACEBOOKID = "facebookId";
    public static final String A001_OUT_KEY = "key";

    public static final String A001_OUT_ERROR_DETAIL = "errors";
	 	 
	/*Fin Parametros Api*/
	
	 /*API new_user_post */
    // INPUT, OUTPUT, error
    public static final String A002_INP_EMAIL = "email";
    public static final String A002_INP_NAME = "name";
    public static final String A002_INP_PASSWORD = "password";

    public static final String A002_OUT_EMAIL = "email";
    public static final String A002_OUT_NAME = "name";
    public static final String A002_OUT_KEY = "key";

    public static final String A002_OUT_ERROR_DETAIL = "errors";
	 	 
	/*Fin Parametros Api*/
	
	/*API login_post */
    // INPUT, OUTPUT, error
    public static final String A003_INP_EMAIL = "email";
    public static final String A003_INP_PASSWORD = "password";

    public static final String A003_OUT_EMAIL = "email";
    public static final String A003_OUT_NAME = "name";
    public static final String A003_OUT_KEY = "key";

    public static final String A003_OUT_ERROR_DETAIL = "errors";

	/*Fin Parametros Api*/

    /**/

}
