package com.angkorteam.mbaas.server.controller;

import com.angkorteam.mbaas.model.entity.Tables;
import com.angkorteam.mbaas.model.entity.tables.JavascriptTable;
import com.angkorteam.mbaas.model.entity.tables.records.JavascriptRecord;
import com.angkorteam.mbaas.plain.request.script.ScriptExecuteRequest;
import com.angkorteam.mbaas.plain.response.script.ScriptExecuteResponse;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by socheat on 2/27/16.
 */
@Controller
@RequestMapping(path = "/javascript")
public class JavascriptController {

    @Autowired
    private DSLContext context;

    @RequestMapping(
            path = "/execute/{script}",
            method = {RequestMethod.POST, RequestMethod.PUT},
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ScriptExecuteResponse> executeJson(
            HttpServletRequest req,
            HttpServletResponse resp,
            @RequestHeader(name = "X-MBAAS-APPCODE", required = false) String appCode,
            @RequestHeader(name = "X-MBAAS-SESSION", required = false) String session,
            @PathVariable("script") String script,
            @RequestBody(required = false) ScriptExecuteRequest requestBody
    ) throws ScriptException {
        JavascriptTable javascriptTable = Tables.JAVASCRIPT.as("javascriptTable");
        JavascriptRecord javascriptRecord = context.select(javascriptTable.fields()).from(javascriptTable).where(javascriptTable.NAME.eq(script)).fetchOneInto(javascriptTable);

        if (javascriptRecord == null || javascriptRecord.getScript() == null || "".equals(javascriptRecord.getScript())) {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.setHttpCode(HttpStatus.METHOD_NOT_ALLOWED.value());
            return ResponseEntity.ok(response);
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.eval(javascriptRecord.getScript());
        Invocable invocable = (Invocable) engine;
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        Http ihttp = null;
        Map<String, Object> responseBody = null;
        if (method == HttpMethod.POST) {
            HttpPost http = invocable.getInterface(HttpPost.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpPost(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.PUT) {
            HttpPut http = invocable.getInterface(HttpPut.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpPut(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        }

        if (ihttp == null) {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.setHttpCode(HttpStatus.METHOD_NOT_ALLOWED.value());
            return ResponseEntity.ok(response);
        } else {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.getData().setScript(script);
            if (responseBody != null) {
                response.getData().getBody().putAll(responseBody);
            }
            return ResponseEntity.ok(response);
        }
    }

    @RequestMapping(
            path = "/execute/{script}",
            method = {RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.TRACE},
            consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ScriptExecuteResponse> execute(
            HttpServletRequest req,
            HttpServletResponse resp,
            @RequestHeader(name = "X-MBAAS-APPCODE", required = false) String appCode,
            @RequestHeader(name = "X-MBAAS-SESSION", required = false) String session,
            @PathVariable("script") String script,
            @RequestBody(required = false) ScriptExecuteRequest requestBody
    ) throws ScriptException {
        JavascriptTable javascriptTable = Tables.JAVASCRIPT.as("javascriptTable");
        JavascriptRecord javascriptRecord = context.select(javascriptTable.fields()).from(javascriptTable).where(javascriptTable.NAME.eq(script)).fetchOneInto(javascriptTable);

        if (javascriptRecord == null || javascriptRecord.getScript() == null || "".equals(javascriptRecord.getScript())) {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.setHttpCode(HttpStatus.METHOD_NOT_ALLOWED.value());
            return ResponseEntity.ok(response);
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");
        engine.eval(javascriptRecord.getScript());
        Invocable invocable = (Invocable) engine;
        HttpMethod method = HttpMethod.valueOf(req.getMethod());
        Http ihttp = null;
        Map<String, Object> responseBody = null;
        if (method == HttpMethod.GET) {
            HttpGet http = invocable.getInterface(HttpGet.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpGet(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.HEAD) {
            HttpHead http = invocable.getInterface(HttpHead.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpHead(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.PATCH) {
            HttpPatch http = invocable.getInterface(HttpPatch.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpPatch(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.DELETE) {
            HttpDelete http = invocable.getInterface(HttpDelete.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpDelete(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.OPTIONS) {
            HttpOptions http = invocable.getInterface(HttpOptions.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpOptions(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        } else if (method == HttpMethod.TRACE) {
            HttpTrace http = invocable.getInterface(HttpTrace.class);
            if (http != null) {
                ihttp = http;
                try {
                    responseBody = http.httpTrace(req, resp);
                } catch (ClassCastException e) {
                    if (!e.getMessage().equals("Cannot cast jdk.nashorn.internal.runtime.Undefined to java.util.Map")) {
                        throw e;
                    }
                }
            }
        }

        if (ihttp == null) {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.setHttpCode(HttpStatus.METHOD_NOT_ALLOWED.value());
            return ResponseEntity.ok(response);
        } else {
            ScriptExecuteResponse response = new ScriptExecuteResponse();
            response.getData().setScript(script);
            if (responseBody != null) {
                response.getData().getBody().putAll(responseBody);
            }
            return ResponseEntity.ok(response);
        }
    }

    @RequestMapping(path = "/create")
    public void create() {
    }

    @RequestMapping(path = "/modify")
    public void modify() {
    }

    public interface Http {
    }

    public interface HttpGet extends Http {
        Map<String, Object> httpGet(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpHead extends Http {
        Map<String, Object> httpHead(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpPost extends Http {
        Map<String, Object> httpPost(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpPut extends Http {
        Map<String, Object> httpPut(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpPatch extends Http {
        Map<String, Object> httpPatch(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpDelete extends Http {
        Map<String, Object> httpDelete(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpOptions extends Http {
        Map<String, Object> httpOptions(HttpServletRequest request, HttpServletResponse response);
    }

    public interface HttpTrace extends Http {
        Map<String, Object> httpTrace(HttpServletRequest request, HttpServletResponse response);
    }
}
