package doext.implement;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import android.text.TextUtils;
import android.util.Base64;
import core.DoServiceContainer;
import core.helper.DoIOHelper;
import core.helper.DoJsonHelper;
import core.interfaces.DoIScriptEngine;
import core.interfaces.DoISourceFS;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import doext.define.do_Algorithm_IMethod;
import doext.implement.util.DoBase64Utils;
import doext.implement.util.DoDESUtil;
import doext.implement.util.DoHexUtils;
import doext.implement.util.DoMD5Utils;
import doext.implement.util.DoSHA1Utils;

/**
 * 自定义扩展SM组件Model实现，继承DoSingletonModule抽象类，并实现do_Algorithm_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.getUniqueKey());
 */
public class do_Algorithm_Model extends DoSingletonModule implements do_Algorithm_IMethod {

	public do_Algorithm_Model() throws Exception {
		super();
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("md5Sync".equals(_methodName)) {
			md5Sync(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("des3Sync".equals(_methodName)) {
			des3Sync(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("sha1Sync".equals(_methodName)) {
			sha1Sync(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("base64Sync".equals(_methodName)) {
			base64Sync(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V），获取参数值使用API提供DoJsonHelper类；
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		if ("md5".equals(_methodName)) {
			md5(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("des3".equals(_methodName)) {
			des3(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("sha1".equals(_methodName)) {
			sha1(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("base64".equals(_methodName)) {
			base64(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("hex2Str".equals(_methodName)) {
			hex2Str(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("hex2Binary".equals(_methodName)) {
			hex2Binary(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		} else if ("xml2Json".equals(_methodName)) {
			xml2Json(_dictParas, _scriptEngine, _callbackFuncName);
			return true;
		}
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * Base64算法；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void base64(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _value = DoJsonHelper.getString(_dictParas, "source", "");
			String _sourceType = DoJsonHelper.getString(_dictParas, "sourceType", "");
			String _result = "";
			if ("file".equals(_sourceType.trim())) {
				if ("encode".equals(_type.trim())) { // 加密
					String _fileFullPath = _scriptEngine.getCurrentApp().getDataFS().getFileFullPathByName(_value);
					if (!DoIOHelper.existFile(_fileFullPath)) {
						throw new Exception(_fileFullPath + " 文件不存在！");
					}
					byte[] readAllBytes = DoIOHelper.readAllBytes(_fileFullPath);
					_result = DoBase64Utils.encode(readAllBytes);
				} else {
					String fileName = getFileNameStr();
					String tempPath = "data://temp/" + fileName;
					String reTempPath = _scriptEngine.getCurrentApp().getDataFS().getFileFullPathByName(tempPath);
					DoIOHelper.writeAllBytes(reTempPath, DoBase64Utils.decode(_value));
					_result = tempPath;
				}
			} else {
				if ("encode".equals(_type.trim())) { // 加密
					_result = DoBase64Utils.encode(_value.getBytes());
				} else { // 解密
					_result = DoIOHelper.getUTF8String(Base64.decode(_value, Base64.DEFAULT));
				}
			}
			_invokeResult.setResultText(_result);
		} catch (Exception ex) {
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm base64 failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	/**
	 * 3DES算法；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void des3(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _key = DoJsonHelper.getString(_dictParas, "key", "");
			String _source = DoJsonHelper.getString(_dictParas, "source", "");
			DoDESUtil des = new DoDESUtil(_key);
			if ("encrypt".equals(_type.trim())) {// 加密
				_invokeResult.setResultText(des.EncriptData(_source));
			} else {
				_invokeResult.setResultText(des.DecriptData(_source));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm des3 failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	/**
	 * md5算法；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void md5(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _value = DoJsonHelper.getString(_dictParas, "value", "");
			String _result = "";
			if ("file".equals(_type.trim())) {
				String _fileFullPath = _scriptEngine.getCurrentApp().getDataFS().getFileFullPathByName(_value);
				if (!DoIOHelper.existFile(_fileFullPath)) {
					throw new Exception(_fileFullPath + " 文件不存在！");
				}
				byte[] readAllBytes = DoIOHelper.readAllBytes(_fileFullPath);
				_result = DoMD5Utils.md5(readAllBytes);
			} else {
				_result = DoMD5Utils.md5(_value);
			}
			_invokeResult.setResultText(_result);
		} catch (Exception ex) {
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm md5 failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	/**
	 * 安全哈希算法；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_callbackFuncName 回调函数名
	 */
	@Override
	public void sha1(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _value = DoJsonHelper.getString(_dictParas, "value", "");
			String _result = DoSHA1Utils.encode(_value);
			if ("uppercase".equals(_type.trim())) {
				_result = _result.toUpperCase(Locale.getDefault());
			} else {
				_result = _result.toLowerCase(Locale.getDefault());
			}
			_invokeResult.setResultText(_result);
		} catch (Exception ex) {
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm sha1 failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	private String getFileNameStr() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	@Override
	public void base64Sync(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _value = DoJsonHelper.getString(_dictParas, "source", "");
			String _result = "";
			if ("encode".equals(_type.trim())) { // 加密
				_result = DoBase64Utils.encode(_value.getBytes());
			} else { // 解密
				_result = DoIOHelper.getUTF8String(Base64.decode(_value, Base64.DEFAULT));
			}
			_invokeResult.setResultText(_result);
		} catch (Exception ex) {
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm base64Sync failed", ex);
		}
	}

	@Override
	public void des3Sync(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		try {
			String _key = DoJsonHelper.getString(_dictParas, "key", "");
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _source = DoJsonHelper.getString(_dictParas, "source", "");
			DoDESUtil des = new DoDESUtil(_key);
			if ("encrypt".equals(_type.trim())) {// 加密
				_invokeResult.setResultText(des.EncriptData(_source));
			} else {
				_invokeResult.setResultText(des.DecriptData(_source));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm des3Sync failed", ex);
		}

	}

	@Override
	public void md5Sync(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		try {
			String _value = DoJsonHelper.getString(_dictParas, "value", "");
			_invokeResult.setResultText(DoMD5Utils.md5(_value));
		} catch (Exception ex) {
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm md5Sync failed", ex);
		}

	}

	@Override
	public void sha1Sync(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		try {
			String _type = DoJsonHelper.getString(_dictParas, "type", "");
			String _value = DoJsonHelper.getString(_dictParas, "value", "");
			String _result = DoSHA1Utils.encode(_value);
			if ("uppercase".equals(_type.trim())) {
				_result = _result.toUpperCase(Locale.getDefault());
			} else {
				_result = _result.toLowerCase(Locale.getDefault());
			}
			_invokeResult.setResultText(_result);
		} catch (Exception ex) {
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm sha1Sync failed", ex);
		}
	}

	public void hex2Str(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _value = DoJsonHelper.getString(_dictParas, "source", "");
			if (TextUtils.isEmpty(_value)) {
				throw new Exception("source不能为空!");
			}
			String _encoding = DoJsonHelper.getString(_dictParas, "encoding", "utf-8");
			byte[] _result = DoHexUtils.hexStringToBytes(_value);
			_invokeResult.setResultText(getString(_result, _encoding));
		} catch (Exception ex) {
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm hex2Str failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	public void hex2Binary(JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) {
		DoInvokeResult _invokeResult = new DoInvokeResult(this.getUniqueKey());
		try {
			String _value = DoJsonHelper.getString(_dictParas, "source", "");
			if (TextUtils.isEmpty(_value)) {
				throw new Exception("source不能为空!");
			}
			String _path = DoJsonHelper.getString(_dictParas, "path", "");
			if (TextUtils.isEmpty(_path)) {
				throw new Exception("path不能为空!");
			}
			if (!_path.startsWith(DoISourceFS.DATA_PREFIX)) {
				throw new Exception("path参数只支持 " + DoISourceFS.DATA_PREFIX + "打头!");
			}
			byte[] _result = DoHexUtils.hexStringToBytes(_value);

			String _fullPath = _scriptEngine.getCurrentApp().getDataFS().getFileFullPathByName(_path);
			DoIOHelper.writeAllBytes(_fullPath, _result);
			_invokeResult.setResultBoolean(true);
		} catch (Exception ex) {
			_invokeResult.setResultBoolean(false);
			_invokeResult.setException(ex);
			DoServiceContainer.getLogEngine().writeError("DoAlgorithm hex2Str failed", ex);
		} finally {
			_scriptEngine.callback(_callbackFuncName, _invokeResult);
		}
	}

	public static String getString(byte[] _src, String _encoding) throws UnsupportedEncodingException {
		if (_src == null)
			return null;
		if (_src.length >= 3 && _src[0] == (byte) 0xef && _src[1] == (byte) 0xbb && _src[2] == (byte) 0xbf) {
			return new String(_src, 3, _src.length - 3, _encoding);
		}
		return new String(_src, 0, _src.length, _encoding);
	}

	@Override
	public void xml2Json(JSONObject _dictParas, final DoIScriptEngine _scriptEngine, final String _callbackFuncName) throws Exception {
		String _source = DoJsonHelper.getString(_dictParas, "source", "");
		if (TextUtils.isEmpty(_source)) {
			throw new Exception("source不能为空！");
		}
		DoInvokeResult _invokeResult = new DoInvokeResult(do_Algorithm_Model.this.getUniqueKey());
		JSONObject jsonObj = null;
		try {
			jsonObj = XML.toJSONObject(_source);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		_invokeResult.setResultText(jsonObj.toString());
		_scriptEngine.callback(_callbackFuncName, _invokeResult);
	}
}