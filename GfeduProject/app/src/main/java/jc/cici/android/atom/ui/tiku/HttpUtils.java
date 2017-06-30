package jc.cici.android.atom.ui.tiku;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

public class HttpUtils {

    private static HttpUtils instance;

    public static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }

    /**
     * 是否联网
     **/
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 正常url请求方式
     *
     * @param url
     * @param params
     * @return
     */
    public synchronized String post(String url, List<NameValuePair> params) {
        HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new UrlEncodedFormEntity(params));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//	/**
//	 * json 请求方式 获取科目测试情况数据
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized TestHistoryInfo getTestInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					TestHistoryInfo infos = JSON.parseObject(body,
//							TestHistoryInfo.class);
//					return infos;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 获取作答正确率情况
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized RatioInfo getRatioInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					RatioInfo infos = JSON.parseObject(body, RatioInfo.class);
//					return infos;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 获取自我测评列表内容
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized SelfTestInfo getSelfTestInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					SelfTestInfo infos = JSON.parseObject(body,
//							SelfTestInfo.class);
//					return infos;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 后台提交自我测评情况
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized ScoreBean getSubmitResponseInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					ScoreBean score = JSON.parseObject(body, ScoreBean.class);
//					return score;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * 获取知识点测评信息
     *
     * @param url
     * @param params
     * @return
     */
    public synchronized String getKnowledageH5Info(String url, Object params) {

        HttpPost post = new HttpPost(url);
        try {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(params.toString()));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                JSONObject obj = new JSONObject(str);
                int code = obj.getInt("Code");
                System.out.println("Code >>>" + code);
                if (100 == code) {
                    String body = obj.getString("Body");
                    JSONObject strObj = new JSONObject(body);
                    String score = strObj.getString("LinkUrl");
                    return score;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//	/**
//	 * 获取科目测试得分情况
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized TestScore getTestScoreInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					TestScore result = JSON
//							.parseObject(body, TestScore.class);
//					return result;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * 获取视频串讲信息
//	 *
//	 * @param url
//	 * @param params
//	 * @return
//	 */
//	public synchronized ConstrueVideoBean getConstrueVideoInfo(String url, Object params) {
//
//		HttpPost post = new HttpPost(url);
//		try {
//			post.addHeader("Content-Type", "application/json");
//			post.setEntity(new StringEntity(params.toString()));
//			HttpClient client = new DefaultHttpClient();
//			HttpResponse response = client.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				String str = EntityUtils.toString(response.getEntity());
//				JSONObject obj = new JSONObject(str);
//				int code = obj.getInt("Code");
//				System.out.println("Code >>>" + code);
//				if (100 == code) {
//					String body = obj.getString("Body");
//					ConstrueVideoBean result = JSON
//							.parseObject(body, ConstrueVideoBean.class);
//					return result;
//				} else {
//					return null;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//


    /**
     * 获取科目测试信息
     *
     * @param url
     * @param params
     * @return
     */
    public synchronized ContentZhenTi getTestResultInfo(String url, Object params) {
        HttpPost post = new HttpPost(url);
        try {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(params.toString()));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                ContentZhenTi result = JSON
                        .parseObject(str, ContentZhenTi.class);
                System.out.println("str -- " + str);
                return result;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public synchronized SubmitQuesAnswer getSubmitQuesAnswer(String url, Object params) {
        System.out.println("url >>>:"+url);
        System.out.println("params >>>:"+new Gson().toJson(params).toString());
        HttpPost post = new HttpPost(url);
        try {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(params.toString()));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                SubmitQuesAnswer result = JSON
                        .parseObject(str, SubmitQuesAnswer.class);
                System.out.println("str -- " + str);
                return result;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized CardStatus getuserpaperreport(String url, Object params) {

        System.out.println("url >>>:"+url);
        System.out.println("params >>>:"+new Gson().toJson(params).toString());
        HttpPost post = new HttpPost(url);
        try {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(params.toString()));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                CardStatus result = JSON
                        .parseObject(str, CardStatus.class);
                System.out.println("str -- " + str);
                return result;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized WrongAnalysisContent getuserpapercarderror(String url, Object params) {
        HttpPost post = new HttpPost(url);
        try {
            post.addHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(params.toString()));
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                String str = EntityUtils.toString(response.getEntity());
                WrongAnalysisContent result = JSON
                        .parseObject(str, WrongAnalysisContent.class);
                System.out.println("WrongAnalysisContent -- " + str);
                return result;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
