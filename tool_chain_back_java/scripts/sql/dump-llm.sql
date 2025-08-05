--
-- PostgreSQL database dump
--

-- Dumped from database version 15.3
-- Dumped by pg_dump version 17.0

-- Started on 2025-08-04 15:32:31

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 306622)
-- Name: job; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA job;


--
-- TOC entry 6 (class 2615 OID 306623)
-- Name: log; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA log;


--
-- TOC entry 7 (class 2615 OID 306624)
-- Name: meta; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA meta;


--
-- TOC entry 11 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 3893 (class 0 OID 0)
-- Dependencies: 11
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 8 (class 2615 OID 306625)
-- Name: reason; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA reason;


--
-- TOC entry 9 (class 2615 OID 306626)
-- Name: register; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA register;


--
-- TOC entry 10 (class 2615 OID 306627)
-- Name: train; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA train;


--
-- TOC entry 283 (class 1255 OID 306628)
-- Name: update_modified_column(); Type: FUNCTION; Schema: register; Owner: -
--

CREATE FUNCTION register.update_modified_column() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.modify_date = now();
    RETURN NEW; 
END;
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 306629)
-- Name: mm_job; Type: TABLE; Schema: job; Owner: -
--

CREATE TABLE job.mm_job (
    id numeric(20,0) NOT NULL,
    job_name character varying(64) NOT NULL,
    job_group character varying(64) NOT NULL,
    invoke_target character varying(500) NOT NULL,
    cron_expression character varying(255),
    misfire_policy character varying(20),
    concurrent character(1),
    status character(1),
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    remark character varying(500)
);


--
-- TOC entry 3894 (class 0 OID 0)
-- Dependencies: 220
-- Name: TABLE mm_job; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON TABLE job.mm_job IS '定时任务调度表';


--
-- TOC entry 3895 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.id; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.id IS '任务ID';


--
-- TOC entry 3896 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.job_name; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.job_name IS '任务名称';


--
-- TOC entry 3897 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.job_group; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.job_group IS '任务组名';


--
-- TOC entry 3898 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.invoke_target; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.invoke_target IS '调用目标字符串';


--
-- TOC entry 3899 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.cron_expression; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.cron_expression IS 'cron执行表达式';


--
-- TOC entry 3900 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.misfire_policy; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.misfire_policy IS '计划执行错误策略（0默认，1立即执行， 2执行一次， 3放弃执行）';


--
-- TOC entry 3901 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.concurrent; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.concurrent IS '是否并发执行（0允许 1禁止）';


--
-- TOC entry 3902 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.status; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.status IS '状态（0正常 1暂停）';


--
-- TOC entry 3903 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.creator_id; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.creator_id IS '创建者';


--
-- TOC entry 3904 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.create_date; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.create_date IS '创建时间';


--
-- TOC entry 3905 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.modifier_id; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.modifier_id IS '更新者';


--
-- TOC entry 3906 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.modify_date; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.modify_date IS '更新时间';


--
-- TOC entry 3907 (class 0 OID 0)
-- Dependencies: 220
-- Name: COLUMN mm_job.remark; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job.remark IS '备注信息';


--
-- TOC entry 221 (class 1259 OID 306636)
-- Name: mm_job_log; Type: TABLE; Schema: job; Owner: -
--

CREATE TABLE job.mm_job_log (
    id numeric(20,0) NOT NULL,
    job_name character varying(64) NOT NULL,
    job_group character varying(64) NOT NULL,
    invoke_target character varying(500) NOT NULL,
    job_message character varying(500),
    status character(1),
    exception_info character varying(2000),
    create_date timestamp without time zone,
    job_id numeric(20,0)
);


--
-- TOC entry 3908 (class 0 OID 0)
-- Dependencies: 221
-- Name: TABLE mm_job_log; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON TABLE job.mm_job_log IS '定时任务调度日志表';


--
-- TOC entry 3909 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.id; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.id IS '任务日志ID';


--
-- TOC entry 3910 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.job_name; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.job_name IS '任务名称';


--
-- TOC entry 3911 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.job_group; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.job_group IS '任务组名';


--
-- TOC entry 3912 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.invoke_target; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.invoke_target IS '调用目标字符串';


--
-- TOC entry 3913 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.job_message; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.job_message IS '日志信息';


--
-- TOC entry 3914 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.status; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.status IS '执行状态（0正常 1失败）';


--
-- TOC entry 3915 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.exception_info; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.exception_info IS '异常信息';


--
-- TOC entry 3916 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.create_date; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.create_date IS '创建时间';


--
-- TOC entry 3917 (class 0 OID 0)
-- Dependencies: 221
-- Name: COLUMN mm_job_log.job_id; Type: COMMENT; Schema: job; Owner: -
--

COMMENT ON COLUMN job.mm_job_log.job_id IS '任务ID，job.mm_job.id';


--
-- TOC entry 222 (class 1259 OID 306641)
-- Name: conversation_history; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.conversation_history (
    id numeric(24,0) NOT NULL,
    user_id character varying(255),
    session_id character varying(255),
    question text,
    answer text,
    intent character varying(255),
    user_state text,
    model_state character varying(255),
    real_question text,
    "time" timestamp without time zone,
    topic_id numeric(10,0)
);


--
-- TOC entry 3918 (class 0 OID 0)
-- Dependencies: 222
-- Name: TABLE conversation_history; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.conversation_history IS '历史记录表';


--
-- TOC entry 3919 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.id IS 'ID';


--
-- TOC entry 3920 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.user_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.user_id IS '用户ID';


--
-- TOC entry 3921 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.session_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.session_id IS '会话ID';


--
-- TOC entry 3922 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.question; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.question IS '用户问题';


--
-- TOC entry 3923 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.answer; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.answer IS '大模型的答案';


--
-- TOC entry 3924 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.intent; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.intent IS '用户意图分类';


--
-- TOC entry 3925 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.user_state; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.user_state IS '本轮对话的关键信息';


--
-- TOC entry 3926 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.model_state; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.model_state IS '本轮对话的模型状态';


--
-- TOC entry 3927 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.real_question; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.real_question IS '改写后的问题';


--
-- TOC entry 3928 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history."time"; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history."time" IS '记录时间';


--
-- TOC entry 3929 (class 0 OID 0)
-- Dependencies: 222
-- Name: COLUMN conversation_history.topic_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.conversation_history.topic_id IS '会话ID（多轮对话）';


--
-- TOC entry 223 (class 1259 OID 306646)
-- Name: mm_log; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_log (
    id numeric(24,0) NOT NULL,
    interface_name character varying(255),
    request_params text,
    response_params text,
    request_time timestamp without time zone DEFAULT now(),
    response_time timestamp without time zone DEFAULT now(),
    duration numeric(10,0),
    status_code character varying(64),
    error_message text,
    client_ip character varying(100),
    server_ip character varying(100),
    creator_id character varying(32),
    create_date timestamp without time zone DEFAULT now(),
    modifier_id character varying(32),
    modify_date timestamp without time zone DEFAULT now(),
    interface_url character varying(255)
);


--
-- TOC entry 3930 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN mm_log.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_log.id IS 'id';


--
-- TOC entry 3931 (class 0 OID 0)
-- Dependencies: 223
-- Name: COLUMN mm_log.interface_url; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_log.interface_url IS '接口地址';


--
-- TOC entry 224 (class 1259 OID 306655)
-- Name: mm_menu_click_log; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_menu_click_log (
    id numeric(20,0) NOT NULL,
    user_id numeric(20,0) NOT NULL,
    menu_url character varying(255),
    menu_name character varying(128),
    click_date date NOT NULL,
    region_code character varying(64),
    user_belong character varying(8),
    source character varying(16)
);


--
-- TOC entry 3932 (class 0 OID 0)
-- Dependencies: 224
-- Name: TABLE mm_menu_click_log; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_menu_click_log IS '菜单点击日志';


--
-- TOC entry 3933 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.id IS '主键';


--
-- TOC entry 3934 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.user_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.user_id IS '用户ID';


--
-- TOC entry 3935 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.menu_url; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.menu_url IS '菜单url';


--
-- TOC entry 3936 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.menu_name; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.menu_name IS '菜单名称';


--
-- TOC entry 3937 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.click_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.click_date IS '点击时间';


--
-- TOC entry 3938 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.region_code; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.region_code IS '区域编码';


--
-- TOC entry 3939 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.user_belong; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.user_belong IS '用户归属（人力账号后两位）';


--
-- TOC entry 3940 (class 0 OID 0)
-- Dependencies: 224
-- Name: COLUMN mm_menu_click_log.source; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_menu_click_log.source IS '来源（tool：工具链；agentplatform：智能体）';


--
-- TOC entry 225 (class 1259 OID 306658)
-- Name: mm_model_chat_log; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_model_chat_log (
    id numeric(24,0) NOT NULL,
    model_chat_type smallint,
    model_chat_id numeric(24,0),
    session_id character varying,
    send_user_id numeric(24,0),
    send_message text,
    send_time timestamp without time zone,
    response_message text,
    response_time timestamp without time zone,
    creator_id numeric(24,0) DEFAULT '-1'::integer,
    create_date timestamp without time zone DEFAULT now(),
    modifier_id numeric(24,0) DEFAULT '-1'::integer,
    modify_date timestamp without time zone DEFAULT now(),
    stream boolean DEFAULT false,
    prompt_tokens integer DEFAULT 0,
    completion_tokens integer DEFAULT 0,
    total_tokens integer DEFAULT 0
);


--
-- TOC entry 3941 (class 0 OID 0)
-- Dependencies: 225
-- Name: TABLE mm_model_chat_log; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_model_chat_log IS '模型体验对话日志';


--
-- TOC entry 3942 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.id IS 'id';


--
-- TOC entry 3943 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.model_chat_type; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.model_chat_type IS '模型对话类型（大模型1；知识助手2；自建3;）';


--
-- TOC entry 3944 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.model_chat_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.model_chat_id IS '模型体验的ID';


--
-- TOC entry 3945 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.session_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.session_id IS '会话ID';


--
-- TOC entry 3946 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.send_user_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.send_user_id IS '发送人';


--
-- TOC entry 3947 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.send_message; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.send_message IS '发送消息';


--
-- TOC entry 3948 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.send_time; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.send_time IS '发送时间';


--
-- TOC entry 3949 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.response_message; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.response_message IS '响应消息';


--
-- TOC entry 3950 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.response_time; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.response_time IS '响应时间';


--
-- TOC entry 3951 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.creator_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.creator_id IS '创建人';


--
-- TOC entry 3952 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.create_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.create_date IS '创建时间';


--
-- TOC entry 3953 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.modifier_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.modifier_id IS '更新人';


--
-- TOC entry 3954 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.modify_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.modify_date IS '更新时间';


--
-- TOC entry 3955 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.stream; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.stream IS '文本推理还是流式推理';


--
-- TOC entry 3956 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.prompt_tokens; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.prompt_tokens IS '提示中的Token数量';


--
-- TOC entry 3957 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.completion_tokens; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.completion_tokens IS '生成的完成中的Token数量';


--
-- TOC entry 3958 (class 0 OID 0)
-- Dependencies: 225
-- Name: COLUMN mm_model_chat_log.total_tokens; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_chat_log.total_tokens IS '整个请求和响应中的总Token数量';


--
-- TOC entry 226 (class 1259 OID 306671)
-- Name: mm_model_monitor_intf; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_model_monitor_intf (
    id integer NOT NULL,
    task_id numeric(20,0) NOT NULL,
    intf_call_type numeric(20,0),
    intf_call_date timestamp without time zone,
    intf_call_date_days numeric(20,0),
    intf_call_date_hours numeric(20,0),
    intf_call_counts numeric(20,0),
    remark character varying(255)
);


--
-- TOC entry 3959 (class 0 OID 0)
-- Dependencies: 226
-- Name: TABLE mm_model_monitor_intf; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_model_monitor_intf IS '模型监控-接口调用统计';


--
-- TOC entry 3960 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.id IS '主键';


--
-- TOC entry 3961 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.task_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.task_id IS '训练任务id;关联mm_train_task';


--
-- TOC entry 3962 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.intf_call_type; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.intf_call_type IS '接口调用状态;0:成功,1:失败';


--
-- TOC entry 3963 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.intf_call_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.intf_call_date IS '接口调用时间';


--
-- TOC entry 3964 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.intf_call_date_days; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.intf_call_date_days IS '接口调用时间-日期';


--
-- TOC entry 3965 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.intf_call_date_hours; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.intf_call_date_hours IS '接口调用时间-小时';


--
-- TOC entry 3966 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.intf_call_counts; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.intf_call_counts IS '接口调用次数';


--
-- TOC entry 3967 (class 0 OID 0)
-- Dependencies: 226
-- Name: COLUMN mm_model_monitor_intf.remark; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_intf.remark IS '备注';


--
-- TOC entry 227 (class 1259 OID 306674)
-- Name: mm_model_monitor_intf_id_seq; Type: SEQUENCE; Schema: log; Owner: -
--

CREATE SEQUENCE log.mm_model_monitor_intf_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3968 (class 0 OID 0)
-- Dependencies: 227
-- Name: mm_model_monitor_intf_id_seq; Type: SEQUENCE OWNED BY; Schema: log; Owner: -
--

ALTER SEQUENCE log.mm_model_monitor_intf_id_seq OWNED BY log.mm_model_monitor_intf.id;


--
-- TOC entry 228 (class 1259 OID 306675)
-- Name: mm_model_monitor_model; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_model_monitor_model (
    id integer NOT NULL,
    task_id numeric(20,0) NOT NULL,
    model_call_date timestamp without time zone,
    model_call_date_days numeric(20,0),
    model_call_date_hours numeric(20,0),
    model_output_token numeric(20,0),
    model_input_token numeric(20,0)
);


--
-- TOC entry 3969 (class 0 OID 0)
-- Dependencies: 228
-- Name: TABLE mm_model_monitor_model; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_model_monitor_model IS '模型监控-模型调用统计';


--
-- TOC entry 3970 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.id IS '主键';


--
-- TOC entry 3971 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.task_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.task_id IS '训练任务id;关联mm_train_task';


--
-- TOC entry 3972 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.model_call_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.model_call_date IS '模型调用时间';


--
-- TOC entry 3973 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.model_call_date_days; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.model_call_date_days IS '模型调用时间-日期';


--
-- TOC entry 3974 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.model_call_date_hours; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.model_call_date_hours IS '模型调用时间-小时';


--
-- TOC entry 3975 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.model_output_token; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.model_output_token IS '模型输出token数';


--
-- TOC entry 3976 (class 0 OID 0)
-- Dependencies: 228
-- Name: COLUMN mm_model_monitor_model.model_input_token; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_model.model_input_token IS '模型输入token数';


--
-- TOC entry 229 (class 1259 OID 306678)
-- Name: mm_model_monitor_model_id_seq; Type: SEQUENCE; Schema: log; Owner: -
--

CREATE SEQUENCE log.mm_model_monitor_model_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3977 (class 0 OID 0)
-- Dependencies: 229
-- Name: mm_model_monitor_model_id_seq; Type: SEQUENCE OWNED BY; Schema: log; Owner: -
--

ALTER SEQUENCE log.mm_model_monitor_model_id_seq OWNED BY log.mm_model_monitor_model.id;


--
-- TOC entry 230 (class 1259 OID 306679)
-- Name: mm_model_monitor_statistics; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_model_monitor_statistics (
    id integer NOT NULL,
    task_id numeric(20,0) NOT NULL,
    token_total numeric(20,0),
    token_input numeric(20,0),
    token_output numeric(20,0),
    intf_total numeric(20,0)
);


--
-- TOC entry 3978 (class 0 OID 0)
-- Dependencies: 230
-- Name: TABLE mm_model_monitor_statistics; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_model_monitor_statistics IS '模型监控日志概括';


--
-- TOC entry 3979 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN mm_model_monitor_statistics.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_statistics.id IS '主键';


--
-- TOC entry 3980 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN mm_model_monitor_statistics.token_total; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_statistics.token_total IS '调用总token数';


--
-- TOC entry 3981 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN mm_model_monitor_statistics.token_input; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_statistics.token_input IS '输入token数';


--
-- TOC entry 3982 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN mm_model_monitor_statistics.token_output; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_statistics.token_output IS '输出token数';


--
-- TOC entry 3983 (class 0 OID 0)
-- Dependencies: 230
-- Name: COLUMN mm_model_monitor_statistics.intf_total; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_model_monitor_statistics.intf_total IS '调用接口总数';


--
-- TOC entry 231 (class 1259 OID 306682)
-- Name: mm_model_monitor_statistics_id_seq; Type: SEQUENCE; Schema: log; Owner: -
--

CREATE SEQUENCE log.mm_model_monitor_statistics_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3984 (class 0 OID 0)
-- Dependencies: 231
-- Name: mm_model_monitor_statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: log; Owner: -
--

ALTER SEQUENCE log.mm_model_monitor_statistics_id_seq OWNED BY log.mm_model_monitor_statistics.id;


--
-- TOC entry 232 (class 1259 OID 306683)
-- Name: mm_user_login_log; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.mm_user_login_log (
    id numeric(20,0) NOT NULL,
    user_id numeric(20,0) NOT NULL,
    login_date timestamp without time zone NOT NULL,
    region_code character varying(64)
);


--
-- TOC entry 3985 (class 0 OID 0)
-- Dependencies: 232
-- Name: TABLE mm_user_login_log; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON TABLE log.mm_user_login_log IS '用户登录日志';


--
-- TOC entry 3986 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN mm_user_login_log.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_user_login_log.id IS '主键';


--
-- TOC entry 3987 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN mm_user_login_log.user_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_user_login_log.user_id IS '用户ID';


--
-- TOC entry 3988 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN mm_user_login_log.login_date; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_user_login_log.login_date IS '登录日期';


--
-- TOC entry 3989 (class 0 OID 0)
-- Dependencies: 232
-- Name: COLUMN mm_user_login_log.region_code; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.mm_user_login_log.region_code IS '区域编码';


--
-- TOC entry 273 (class 1259 OID 327380)
-- Name: session_cache; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.session_cache (
    id bigint NOT NULL,
    province character varying(255) NOT NULL,
    session_id character varying(255) NOT NULL,
    intention character varying(255) NOT NULL,
    session_num integer DEFAULT 0 NOT NULL,
    update_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 3990 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.id IS '自增主键';


--
-- TOC entry 3991 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.province; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.province IS '省份名称';


--
-- TOC entry 3992 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.session_id; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.session_id IS '会话ID';


--
-- TOC entry 3993 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.intention; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.intention IS '意图';


--
-- TOC entry 3994 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.session_num; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.session_num IS '会话次数';


--
-- TOC entry 3995 (class 0 OID 0)
-- Dependencies: 273
-- Name: COLUMN session_cache.update_time; Type: COMMENT; Schema: log; Owner: -
--

COMMENT ON COLUMN log.session_cache.update_time IS '上次交互日期';


--
-- TOC entry 272 (class 1259 OID 327379)
-- Name: session_cache_id_seq; Type: SEQUENCE; Schema: log; Owner: -
--

ALTER TABLE log.session_cache ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME log.session_cache_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 233 (class 1259 OID 306686)
-- Name: mm_application_square; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_application_square (
    id numeric(20,0) NOT NULL,
    name character varying(64) NOT NULL,
    type character varying(8),
    endpoint character varying(512),
    description text,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    scene character varying(8),
    uid character varying(64),
    experience character varying(4) DEFAULT 1,
    belong character varying(4) DEFAULT 2
);


--
-- TOC entry 3996 (class 0 OID 0)
-- Dependencies: 233
-- Name: TABLE mm_application_square; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_application_square IS '应用广场表';


--
-- TOC entry 3997 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.id IS '主键';


--
-- TOC entry 3998 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.name IS '应用名称';


--
-- TOC entry 3999 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.type IS '应用类型,字典APPLICATION_TYPE';


--
-- TOC entry 4000 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.endpoint; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.endpoint IS '应用接口地址';


--
-- TOC entry 4001 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.description IS '应用描述';


--
-- TOC entry 4002 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.creator_id IS '创建人';


--
-- TOC entry 4003 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.create_date IS '创建时间';


--
-- TOC entry 4004 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.modifier_id IS '更新人';


--
-- TOC entry 4005 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.modify_date IS '更新时间';


--
-- TOC entry 4006 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.scene; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.scene IS '场景, 字典APPLICATION_SCENE';


--
-- TOC entry 4007 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.uid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.uid IS '身份识别码（dcoos平台接口时为数据EOP平台的识别码）';


--
-- TOC entry 4008 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.experience; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.experience IS '是否可体验（0是，1否）';


--
-- TOC entry 4009 (class 0 OID 0)
-- Dependencies: 233
-- Name: COLUMN mm_application_square.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_application_square.belong IS '应用归属，字典APPLICATION_BELONG';


--
-- TOC entry 234 (class 1259 OID 306695)
-- Name: mm_cluster; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_cluster (
    id numeric(20,0) NOT NULL,
    name character varying(128),
    code character varying(32),
    province character varying(20),
    prometheus_host_url character varying(128),
    train_host_url character varying(128),
    deploy_host_url character varying(128),
    reason_host_url character varying(128),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    node_host_url character varying(128)
);


--
-- TOC entry 4010 (class 0 OID 0)
-- Dependencies: 234
-- Name: TABLE mm_cluster; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_cluster IS '集群资源';


--
-- TOC entry 4011 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.id IS '主键';


--
-- TOC entry 4012 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.name IS '名称';


--
-- TOC entry 4013 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.code IS '编码';


--
-- TOC entry 4014 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.province; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.province IS '省份';


--
-- TOC entry 4015 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.prometheus_host_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.prometheus_host_url IS 'Prometheus监控服务地址http://ip:port';


--
-- TOC entry 4016 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.train_host_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.train_host_url IS '训练任务服务地址http://ip:port';


--
-- TOC entry 4017 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.deploy_host_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.deploy_host_url IS '部署任务服务地址http://ip:port';


--
-- TOC entry 4018 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.reason_host_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.reason_host_url IS '推理服务地址http://ip:port';


--
-- TOC entry 4019 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.creator_id IS '创建人';


--
-- TOC entry 4020 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.create_date IS '创建时间';


--
-- TOC entry 4021 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.modifier_id IS '更新人';


--
-- TOC entry 4022 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.modify_date IS '更新时间';


--
-- TOC entry 4023 (class 0 OID 0)
-- Dependencies: 234
-- Name: COLUMN mm_cluster.node_host_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster.node_host_url IS '获取集群节点信息服务地址http://ip:port';


--
-- TOC entry 235 (class 1259 OID 306702)
-- Name: mm_cluster_metric; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_cluster_metric (
    id numeric(20,0) NOT NULL,
    name character varying(128),
    code character varying(32) NOT NULL,
    entry_field character varying(32),
    cluster_code character varying(32) NOT NULL,
    category character varying(32),
    expression character varying(512),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    remarks character varying(512),
    unit character varying(8),
    result_list character varying(16) DEFAULT '1'::character varying
);


--
-- TOC entry 4024 (class 0 OID 0)
-- Dependencies: 235
-- Name: TABLE mm_cluster_metric; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_cluster_metric IS '集群指标';


--
-- TOC entry 4025 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.id IS '主键';


--
-- TOC entry 4026 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.name IS '名称';


--
-- TOC entry 4027 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.code IS '编码';


--
-- TOC entry 4028 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.entry_field; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.entry_field IS '实体字段名';


--
-- TOC entry 4029 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.cluster_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.cluster_code IS '集群编码 meta.mm_cluster.code';


--
-- TOC entry 4030 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.category; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.category IS '分类，资源计数RESOURCE_COUNT；集群资源使用情况RESOURCE_USAGE;集群详情CLUSTER_DETAIL;集群使用趋势CLUSTER_USAGE_TREND;使用资源RESOURCE_USAGE_DETAIL';


--
-- TOC entry 4031 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.expression; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.expression IS '表达式';


--
-- TOC entry 4032 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.create_date IS '创建时间';


--
-- TOC entry 4033 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.modify_date IS '更新时间';


--
-- TOC entry 4034 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.remarks; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.remarks IS '备注';


--
-- TOC entry 4035 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.unit; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.unit IS '指标单位';


--
-- TOC entry 4036 (class 0 OID 0)
-- Dependencies: 235
-- Name: COLUMN mm_cluster_metric.result_list; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_cluster_metric.result_list IS '结果是否是列表（0是，1否）';


--
-- TOC entry 236 (class 1259 OID 306710)
-- Name: mm_data_set; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_data_set (
    id numeric(20,0) NOT NULL,
    data_set_name character varying(64) NOT NULL,
    data_set_version character varying(8),
    data_type character varying(32),
    pr_count bigint,
    set_type character varying(32),
    save_type character varying(32),
    save_path character varying(255) NOT NULL,
    template_type character varying(32),
    description character varying(512),
    call_count bigint DEFAULT 0,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_valid character varying(8) DEFAULT 10050001,
    is_delete character varying(8) DEFAULT 10050002,
    type character varying DEFAULT 2,
    belong character varying NOT NULL,
    file_size bigint DEFAULT 0 NOT NULL,
    request_id numeric(20,0),
    region_code character varying(64),
    enhanced_train_task_id numeric(20,0),
    test_set_evaluation_id numeric(20,0),
    project_id numeric(24,0)
);


--
-- TOC entry 4037 (class 0 OID 0)
-- Dependencies: 236
-- Name: TABLE mm_data_set; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_data_set IS '数据集';


--
-- TOC entry 4038 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.id IS 'id';


--
-- TOC entry 4039 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.data_set_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.data_set_name IS '数据集名称;数据集_20240514_104036';


--
-- TOC entry 4040 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.data_set_version; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.data_set_version IS '测试数据集版本';


--
-- TOC entry 4041 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.data_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.data_type IS '数据类型;(1Prompt+Response; 2时序数据; 3意图识别）';


--
-- TOC entry 4042 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.pr_count; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.pr_count IS '对话数据量;1222';


--
-- TOC entry 4043 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.set_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.set_type IS '数据集分类(1训练数据集; 2测试数据集)';


--
-- TOC entry 4044 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.save_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.save_type IS '保存类型;平台共享存储';


--
-- TOC entry 4045 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.save_path; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.save_path IS '保存路径;/tmp/text.json';


--
-- TOC entry 4046 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.template_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.template_type IS '模板类型;jsonl模板';


--
-- TOC entry 4047 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.description IS '数据集描述';


--
-- TOC entry 4048 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.call_count; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.call_count IS '调用次数;1';


--
-- TOC entry 4049 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.creator_id IS '创建人id;1';


--
-- TOC entry 4050 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.create_date IS '创建时间';


--
-- TOC entry 4051 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.modifier_id IS '修改人id';


--
-- TOC entry 4052 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.modify_date IS '修改时间';


--
-- TOC entry 4053 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.is_valid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.is_valid IS '是否有效';


--
-- TOC entry 4054 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.is_delete; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.is_delete IS '是否删除';


--
-- TOC entry 4055 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.type IS '类别,多个以逗号隔开(网络, 运维，客服...)';


--
-- TOC entry 4056 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.belong IS '归属';


--
-- TOC entry 4057 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.file_size; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.file_size IS '问答对大小(单位:B)';


--
-- TOC entry 4058 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.request_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.request_id IS '请求id';


--
-- TOC entry 4059 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.region_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.region_code IS '区域编码';


--
-- TOC entry 4060 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.enhanced_train_task_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.enhanced_train_task_id IS '绑定强化训练任务id';


--
-- TOC entry 4061 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.test_set_evaluation_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.test_set_evaluation_id IS '绑定测试集评估id';


--
-- TOC entry 4062 (class 0 OID 0)
-- Dependencies: 236
-- Name: COLUMN mm_data_set.project_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set.project_id IS '项目空间id';


--
-- TOC entry 237 (class 1259 OID 306722)
-- Name: mm_data_set_file; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_data_set_file (
    id numeric(20,0) NOT NULL,
    request_id numeric(20,0),
    template_type character varying(8),
    save_path character varying(512),
    file_size bigint,
    pr_count bigint,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_valid character varying(8) DEFAULT 10050001,
    is_delete character varying(8) DEFAULT 10050002,
    belong character varying
);


--
-- TOC entry 4063 (class 0 OID 0)
-- Dependencies: 237
-- Name: TABLE mm_data_set_file; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_data_set_file IS '数据集上传文件';


--
-- TOC entry 4064 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.id IS 'id';


--
-- TOC entry 4065 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.request_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.request_id IS '请求id;数据集_20240514_104036';


--
-- TOC entry 4066 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.template_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.template_type IS '文件类型';


--
-- TOC entry 4067 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.save_path; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.save_path IS '保存路径';


--
-- TOC entry 4068 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.file_size; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.file_size IS '文件大小(单位：B)';


--
-- TOC entry 4069 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.pr_count; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.pr_count IS '对话数';


--
-- TOC entry 4070 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.creator_id IS '创建人id;1';


--
-- TOC entry 4071 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.create_date IS '创建时间';


--
-- TOC entry 4072 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.modifier_id IS '修改人id';


--
-- TOC entry 4073 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.modify_date IS '修改时间';


--
-- TOC entry 4074 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.is_valid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.is_valid IS '是否有效';


--
-- TOC entry 4075 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.is_delete; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.is_delete IS '是否删除';


--
-- TOC entry 4076 (class 0 OID 0)
-- Dependencies: 237
-- Name: COLUMN mm_data_set_file.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_data_set_file.belong IS '归属(1知识库;2用户上传)';


--
-- TOC entry 238 (class 1259 OID 306731)
-- Name: mm_dict_data; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_dict_data (
    dict_code numeric(20,0) NOT NULL,
    dict_sort integer DEFAULT 0,
    dict_label character varying(100),
    dict_value character varying(100),
    dict_type character varying(100),
    css_class character varying(100),
    list_class character varying(100),
    is_default character(1),
    status character(1),
    creator_id numeric(20,0),
    create_date timestamp(6) without time zone,
    modifier_id numeric(20,0),
    modify_date timestamp(6) without time zone,
    remark character varying(500),
    parent_id numeric(20,0),
    ext_field1 character varying(256),
    ext_field2 character varying(256),
    ext_field3 character varying(256)
);


--
-- TOC entry 4077 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.dict_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.dict_code IS 'ID';


--
-- TOC entry 4078 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.dict_sort; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.dict_sort IS '排序';


--
-- TOC entry 4079 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.dict_label; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.dict_label IS '字典项名称';


--
-- TOC entry 4080 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.dict_value; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.dict_value IS '字典项值';


--
-- TOC entry 4081 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.dict_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.dict_type IS '字典类型编码，meta.mm_dict_type.dict_type';


--
-- TOC entry 4082 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.css_class; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.css_class IS 'css样式';


--
-- TOC entry 4083 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.list_class; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.list_class IS '列表样式';


--
-- TOC entry 4084 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.is_default; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.is_default IS '是否默认';


--
-- TOC entry 4085 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.status IS '状态，0启用，1禁用';


--
-- TOC entry 4086 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.creator_id IS '创建人ID';


--
-- TOC entry 4087 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.create_date IS '创建时间';


--
-- TOC entry 4088 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.modifier_id IS '修改人ID';


--
-- TOC entry 4089 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.modify_date IS '修改时间';


--
-- TOC entry 4090 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.remark IS '备注';


--
-- TOC entry 4091 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.parent_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.parent_id IS '父ID';


--
-- TOC entry 4092 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.ext_field1; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.ext_field1 IS '扩展字段1';


--
-- TOC entry 4093 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.ext_field2; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.ext_field2 IS '扩展字段2';


--
-- TOC entry 4094 (class 0 OID 0)
-- Dependencies: 238
-- Name: COLUMN mm_dict_data.ext_field3; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_data.ext_field3 IS '扩展字段3';


--
-- TOC entry 239 (class 1259 OID 306737)
-- Name: mm_dict_type; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_dict_type (
    dict_id numeric(20,0) NOT NULL,
    dict_name character varying(255),
    dict_type character varying(100),
    status character(1),
    creator_id numeric(20,0),
    create_date timestamp(6) without time zone,
    modifier_id numeric(20,0),
    modify_date timestamp(6) without time zone,
    remark character varying(500),
    parent_id numeric(20,0)
);


--
-- TOC entry 4095 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.dict_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.dict_id IS 'ID';


--
-- TOC entry 4096 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.dict_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.dict_name IS '字典名称';


--
-- TOC entry 4097 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.dict_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.dict_type IS '编码';


--
-- TOC entry 4098 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.status IS '状态。0启用，1 禁用';


--
-- TOC entry 4099 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.creator_id IS '创建人ID';


--
-- TOC entry 4100 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.create_date IS '创建时间';


--
-- TOC entry 4101 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.modifier_id IS '修改人ID';


--
-- TOC entry 4102 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.modify_date IS '修改时间';


--
-- TOC entry 4103 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.remark IS '备注';


--
-- TOC entry 4104 (class 0 OID 0)
-- Dependencies: 239
-- Name: COLUMN mm_dict_type.parent_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_dict_type.parent_id IS '父ID';


--
-- TOC entry 240 (class 1259 OID 306742)
-- Name: mm_domain; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_domain (
    id numeric(20,0) NOT NULL,
    code character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp(0) without time zone NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp(0) without time zone NOT NULL,
    is_valid numeric(8,0) NOT NULL,
    notes character varying(4000),
    domain_type_id numeric(8,0)
);


--
-- TOC entry 241 (class 1259 OID 306747)
-- Name: mm_group; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_group (
    id numeric(20,0) NOT NULL,
    name character varying(64),
    is_valid integer DEFAULT 0,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4105 (class 0 OID 0)
-- Dependencies: 241
-- Name: TABLE mm_group; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_group IS '用户组';


--
-- TOC entry 4106 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.id IS '主键';


--
-- TOC entry 4107 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.name IS '组名';


--
-- TOC entry 4108 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.is_valid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.is_valid IS '是否合理(0是; 1否)';


--
-- TOC entry 4109 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.creator_id IS '创建人';


--
-- TOC entry 4110 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.create_date IS '创建时间';


--
-- TOC entry 4111 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.modifier_id IS '更新人';


--
-- TOC entry 4112 (class 0 OID 0)
-- Dependencies: 241
-- Name: COLUMN mm_group.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group.modify_date IS '更新时间';


--
-- TOC entry 242 (class 1259 OID 306753)
-- Name: mm_group_user; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_group_user (
    id numeric(20,0) NOT NULL,
    group_id numeric(20,0),
    user_id numeric(20,0),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4113 (class 0 OID 0)
-- Dependencies: 242
-- Name: TABLE mm_group_user; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_group_user IS '用户组_用户关系';


--
-- TOC entry 4114 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.id IS '主键';


--
-- TOC entry 4115 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.group_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.group_id IS '用户组id';


--
-- TOC entry 4116 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.user_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.user_id IS '用户id';


--
-- TOC entry 4117 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.creator_id IS '创建人';


--
-- TOC entry 4118 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.create_date IS '创建时间';


--
-- TOC entry 4119 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.modifier_id IS '更新人';


--
-- TOC entry 4120 (class 0 OID 0)
-- Dependencies: 242
-- Name: COLUMN mm_group_user.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_group_user.modify_date IS '更新时间';


--
-- TOC entry 282 (class 1259 OID 379569)
-- Name: mm_host; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_host (
    id numeric(20,0) NOT NULL,
    host_name character varying(100),
    host_ip character varying(100),
    host_type character varying(100),
    cluster_code character varying(20),
    gpu_num numeric(10,0),
    status character varying(20),
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    remark character varying
);


--
-- TOC entry 4121 (class 0 OID 0)
-- Dependencies: 282
-- Name: TABLE mm_host; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_host IS '主机信息';


--
-- TOC entry 4122 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.id IS 'ip';


--
-- TOC entry 4123 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.host_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.host_name IS '主机名称';


--
-- TOC entry 4124 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.host_ip; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.host_ip IS '主机ip';


--
-- TOC entry 4125 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.host_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.host_type IS '主机类型';


--
-- TOC entry 4126 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.cluster_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.cluster_code IS '集群名';


--
-- TOC entry 4127 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.gpu_num; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.gpu_num IS 'GPU卡数';


--
-- TOC entry 4128 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.status IS '占用情况 ：occupy:占用 free:空闲';


--
-- TOC entry 4129 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.create_date IS '创建时间';


--
-- TOC entry 4130 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.update_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.update_date IS '更新时间';


--
-- TOC entry 4131 (class 0 OID 0)
-- Dependencies: 282
-- Name: COLUMN mm_host.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_host.remark IS '备注';


--
-- TOC entry 243 (class 1259 OID 306758)
-- Name: mm_model; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_model (
    id numeric(20,0) NOT NULL,
    name character varying(64) NOT NULL,
    belong character varying(16) NOT NULL,
    alias character varying(64) NOT NULL,
    endpoint character varying(512),
    status character varying(8),
    stop_token_ids character varying(128),
    top_k numeric(10,0),
    max_tokens numeric(10,0),
    optimizable character varying(4) DEFAULT 1,
    trainable character varying(4) DEFAULT 1,
    experience character varying(4) DEFAULT 1,
    reasonable character varying(4) DEFAULT 1,
    deployable character varying(4) DEFAULT 1,
    publishable character varying(4) DEFAULT 1,
    description text,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    uid character varying(64),
    experience_shape character varying(16),
    train_target character varying(16),
    experience_impl integer DEFAULT 1,
    type character varying(8),
    region_code character varying(64),
    model_id bigint,
    access_parameters text,
    deploy_status character varying(255),
    reasoning_model character varying(64)
);


--
-- TOC entry 4132 (class 0 OID 0)
-- Dependencies: 243
-- Name: TABLE mm_model; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_model IS '模型表';


--
-- TOC entry 4133 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.id IS '主键';


--
-- TOC entry 4134 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.name IS '模型名称';


--
-- TOC entry 4135 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.belong IS '归属，详见字典MODEL_BELONG';


--
-- TOC entry 4136 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.alias; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.alias IS '别名，用于大模型接口参数';


--
-- TOC entry 4137 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.endpoint; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.endpoint IS '模型接口地址';


--
-- TOC entry 4138 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.status IS '是否可用（0可用，1不可用）';


--
-- TOC entry 4139 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.stop_token_ids; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.stop_token_ids IS '停止标识符ID数组，用于控制输出停止的位置。多个逗号分隔。';


--
-- TOC entry 4140 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.top_k; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.top_k IS '采样策略参数，用于控制生成文本的多样性。(启明大模型专用)';


--
-- TOC entry 4141 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.max_tokens; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.max_tokens IS '控制生成文本的最大长度，单位是token数。';


--
-- TOC entry 4142 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.optimizable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.optimizable IS '是否可优化（0是，1否）';


--
-- TOC entry 4143 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.trainable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.trainable IS '是否可训练（0是，1否）';


--
-- TOC entry 4144 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.experience; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.experience IS '是否可体验（0是，1否）';


--
-- TOC entry 4145 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.reasonable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.reasonable IS '是否可推理（0是，1否）';


--
-- TOC entry 4146 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.deployable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.deployable IS '是否可部署（0是，1否）';


--
-- TOC entry 4147 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.publishable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.publishable IS '是否可发布/上线（0是，1否）';


--
-- TOC entry 4148 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.description IS '模型描述';


--
-- TOC entry 4149 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.creator_id IS '创建人';


--
-- TOC entry 4150 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.create_date IS '创建时间';


--
-- TOC entry 4151 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.modifier_id IS '更新人';


--
-- TOC entry 4152 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.modify_date IS '更新时间';


--
-- TOC entry 4153 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.uid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.uid IS '身份识别码（dcoos平台接口时为数据EOP平台的识别码）';


--
-- TOC entry 4154 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.experience_shape; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.experience_shape IS '体验形式，详见字典MODEL_EXPERIENCE_SHAPE';


--
-- TOC entry 4155 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.train_target; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.train_target IS '训练接口目标，GZ：贵州，QD青岛，字典TRAIN_DEPLOY_TARGET';


--
-- TOC entry 4156 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.experience_impl; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.experience_impl IS '体验实现方式，同字典APPLICATION_TYPE';


--
-- TOC entry 4157 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.type IS '模型类型，字典MODEL_TYPE';


--
-- TOC entry 4158 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.region_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.region_code IS '区域编码，meta.mm_province.code';


--
-- TOC entry 4159 (class 0 OID 0)
-- Dependencies: 243
-- Name: COLUMN mm_model.reasoning_model; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model.reasoning_model IS '推理模型名称';


--
-- TOC entry 244 (class 1259 OID 306772)
-- Name: mm_model_train; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_model_train (
    id numeric(20,0) NOT NULL,
    model_id numeric(20,0),
    train_method character varying(255),
    train_type character varying(16),
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type character varying(16)
);


--
-- TOC entry 4160 (class 0 OID 0)
-- Dependencies: 244
-- Name: TABLE mm_model_train; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_model_train IS '模型训练配置';


--
-- TOC entry 4161 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.id IS '主键';


--
-- TOC entry 4162 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.model_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.model_id IS '模型ID。meta.mm_model.id';


--
-- TOC entry 4163 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.train_method; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.train_method IS '训练方法，多个逗号分隔，详见字典TRAIN_TASK_METHOD';


--
-- TOC entry 4164 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.train_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.train_type IS '训练类型，详见字典MODEL_TRAIN_TYPE';


--
-- TOC entry 4165 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.creator_id IS '创建人';


--
-- TOC entry 4166 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.create_date IS '创建时间';


--
-- TOC entry 4167 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.modifier_id IS '更新人';


--
-- TOC entry 4168 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.modify_date IS '更新时间';


--
-- TOC entry 4169 (class 0 OID 0)
-- Dependencies: 244
-- Name: COLUMN mm_model_train.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train.type IS '类型，有model.id的为specifical（特定的），没有的为normal（通用的）';


--
-- TOC entry 245 (class 1259 OID 306777)
-- Name: mm_model_train_param; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_model_train_param (
    id numeric(20,0) NOT NULL,
    display_name character varying(64) NOT NULL,
    be_key character varying(64),
    fe_key character varying(64),
    type character varying(16),
    "group" character varying(8),
    check_type character varying(16),
    check_value character varying(512),
    step numeric(20,10),
    default_value character varying(64),
    description text,
    creator_id numeric(24,0) DEFAULT '-1'::integer NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) DEFAULT '-1'::integer NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    model_train_id numeric(20,0)
);


--
-- TOC entry 4170 (class 0 OID 0)
-- Dependencies: 245
-- Name: TABLE mm_model_train_param; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_model_train_param IS '模型训练超参';


--
-- TOC entry 4171 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.id IS '主键';


--
-- TOC entry 4172 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.display_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.display_name IS '前端显示名称';


--
-- TOC entry 4173 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.be_key; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.be_key IS '后端使用的键名';


--
-- TOC entry 4174 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.fe_key; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.fe_key IS '前端使用的键名';


--
-- TOC entry 4175 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.type IS '参数类型（例如：int, float等）';


--
-- TOC entry 4176 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param."group"; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param."group" IS '分组信息';


--
-- TOC entry 4177 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.check_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.check_type IS '校验类型（例如：range, choice等）';


--
-- TOC entry 4178 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.check_value; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.check_value IS '校验值（根据校验类型，可能是范围、选择列表等）';


--
-- TOC entry 4179 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.step; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.step IS '步长值';


--
-- TOC entry 4180 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.default_value; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.default_value IS '默认值';


--
-- TOC entry 4181 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.description IS '超参描述';


--
-- TOC entry 4182 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.creator_id IS '创建人';


--
-- TOC entry 4183 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.create_date IS '创建时间';


--
-- TOC entry 4184 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.modifier_id IS '更新人';


--
-- TOC entry 4185 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.modify_date IS '更新时间';


--
-- TOC entry 4186 (class 0 OID 0)
-- Dependencies: 245
-- Name: COLUMN mm_model_train_param.model_train_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_model_train_param.model_train_id IS '模型训练ID，meta.mm_model_train_param.id';


--
-- TOC entry 246 (class 1259 OID 306786)
-- Name: mm_order; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_order (
    id numeric(20,0) NOT NULL,
    order_title character varying(200) NOT NULL,
    order_code character varying(100),
    "timestamp" character varying(20),
    create_oper_name character varying(20),
    create_oper_org_name character varying(100),
    create_oper_phone character varying(20),
    reference_system character varying(8),
    is_requirement character varying(8),
    requirement_number character varying(100),
    degree_urgency character varying(8),
    requesting_area character varying(20),
    requesting_department character varying(100),
    demand_contact character varying(20),
    demand_contact_phone character varying(20),
    cover_people character varying,
    expected_transfer_volume character varying,
    requirements_overview character varying,
    create_date timestamp without time zone,
    modify_date timestamp without time zone,
    creator_id numeric(20,0),
    modifier_id numeric(20,0),
    remark character varying,
    review_status character varying(8) DEFAULT 1,
    review_time timestamp without time zone,
    upload_status character varying(10) DEFAULT 1
);


--
-- TOC entry 4187 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.id IS 'id';


--
-- TOC entry 4188 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.order_title; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.order_title IS '工单标题';


--
-- TOC entry 4189 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.order_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.order_code IS '工单号';


--
-- TOC entry 4190 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order."timestamp"; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order."timestamp" IS '时间戳';


--
-- TOC entry 4191 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.create_oper_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.create_oper_name IS '工单发起人姓名';


--
-- TOC entry 4192 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.create_oper_org_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.create_oper_org_name IS '工单发起人部门';


--
-- TOC entry 4193 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.create_oper_phone; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.create_oper_phone IS '工单发起人电话';


--
-- TOC entry 4194 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.reference_system; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.reference_system IS '涉及系统
训推工具链：1
智能体工具链：2
训推工具链,智能体工具链：1,2
传编码，例：1
';


--
-- TOC entry 4195 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.is_requirement; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.is_requirement IS '是否关联需求工单
是：1
否：2
传编码，例：1';


--
-- TOC entry 4196 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.requirement_number; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.requirement_number IS '需求工单号
当‘是否关联需求工单’为1是，此项必填';


--
-- TOC entry 4197 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.degree_urgency; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.degree_urgency IS '紧急程度
紧急：1
普通：2
传编码，例：1';


--
-- TOC entry 4198 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.requesting_area; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.requesting_area IS '省份/公司';


--
-- TOC entry 4199 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.requesting_department; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.requesting_department IS '需求提出部门';


--
-- TOC entry 4200 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.demand_contact; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.demand_contact IS '需求联系人';


--
-- TOC entry 4201 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.demand_contact_phone; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.demand_contact_phone IS '联系方式';


--
-- TOC entry 4202 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.cover_people; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.cover_people IS '预计覆盖人数';


--
-- TOC entry 4203 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.expected_transfer_volume; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.expected_transfer_volume IS '预计月调用量';


--
-- TOC entry 4204 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.requirements_overview; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.requirements_overview IS '需求概述';


--
-- TOC entry 4205 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.create_date IS '创建时间';


--
-- TOC entry 4206 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.modify_date IS '更新时间';


--
-- TOC entry 4207 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.creator_id IS '创建人';


--
-- TOC entry 4208 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.modifier_id IS '更新人';


--
-- TOC entry 4209 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.remark IS '备注';


--
-- TOC entry 4210 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.review_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.review_status IS '审核状态：
1：审核中
2：审核结束';


--
-- TOC entry 4211 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.review_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.review_time IS '审核时间';


--
-- TOC entry 4212 (class 0 OID 0)
-- Dependencies: 246
-- Name: COLUMN mm_order.upload_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order.upload_status IS '是否上传门户 0是，1否 字典YES_OR_NO';


--
-- TOC entry 247 (class 1259 OID 306793)
-- Name: mm_order_node; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_order_node (
    id numeric(20,0) NOT NULL,
    order_code character varying(100),
    node_code character varying(100),
    node_name character varying(100),
    node_todo_staff_id character varying(1000),
    node_todo_staff_name character varying(1000),
    node_start_time character varying(50),
    node_status character varying(20),
    node_do_staff_id character varying(1000),
    node_do_staff_name character varying(1000),
    node_end_time character varying(50),
    node_opinions character varying,
    create_date timestamp without time zone,
    modify_date timestamp without time zone,
    creator_id numeric(20,0),
    modifier_id numeric(20,0),
    remark character varying,
    order_id numeric(20,0) NOT NULL
);


--
-- TOC entry 4213 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.id IS 'id';


--
-- TOC entry 4214 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.order_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.order_code IS '环节编码';


--
-- TOC entry 4215 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_code IS '环节编码';


--
-- TOC entry 4216 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_name IS '环节名称';


--
-- TOC entry 4217 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_todo_staff_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_todo_staff_id IS '待处理人id
门户主数据对应的用户账号和名称，多个人逗号(,)分割';


--
-- TOC entry 4218 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_todo_staff_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_todo_staff_name IS '待处理人姓名';


--
-- TOC entry 4219 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_start_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_start_time IS '当前环节开始时间
精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”';


--
-- TOC entry 4220 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_status IS '环节状态
环节处理状态：
0 同意
1 不同意';


--
-- TOC entry 4221 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_do_staff_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_do_staff_id IS '实际处理人id';


--
-- TOC entry 4222 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_do_staff_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_do_staff_name IS '实际处理人姓名';


--
-- TOC entry 4223 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_end_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_end_time IS '环节实际完成时间
精确到秒，日期格式“YYYY-MM-DD HH:MI:SS”';


--
-- TOC entry 4224 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.node_opinions; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.node_opinions IS '环节处理意见';


--
-- TOC entry 4225 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.create_date IS '创建时间';


--
-- TOC entry 4226 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.modify_date IS '更新时间';


--
-- TOC entry 4227 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.creator_id IS '创建人';


--
-- TOC entry 4228 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.modifier_id IS '更新人';


--
-- TOC entry 4229 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.remark IS '备注';


--
-- TOC entry 4230 (class 0 OID 0)
-- Dependencies: 247
-- Name: COLUMN mm_order_node.order_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_node.order_id IS '工单id';


--
-- TOC entry 248 (class 1259 OID 306798)
-- Name: mm_order_user; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_order_user (
    id numeric(20,0) NOT NULL,
    order_code character varying(100),
    tool_chain_name character varying(20),
    tool_chain_oa_name character varying(20),
    province_company character varying(100),
    tool_chain_phone character varying(20),
    permission_state character varying(8),
    create_date timestamp without time zone,
    modify_date timestamp without time zone,
    creator_id numeric(20,0),
    modifier_id numeric(20,0),
    remark character varying,
    pass_status character varying(8) DEFAULT 1,
    pass_time timestamp without time zone,
    order_id numeric(20,0) NOT NULL
);


--
-- TOC entry 4231 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.id IS 'id';


--
-- TOC entry 4232 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.tool_chain_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.tool_chain_name IS '姓名';


--
-- TOC entry 4233 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.tool_chain_oa_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.tool_chain_oa_name IS '人力账号';


--
-- TOC entry 4234 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.province_company; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.province_company IS '省份/公司';


--
-- TOC entry 4235 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.tool_chain_phone; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.tool_chain_phone IS '手机号';


--
-- TOC entry 4236 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.permission_state; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.permission_state IS '工具链权限状态
开通权限：1
关闭权限：2
账号异常：3';


--
-- TOC entry 4237 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.create_date IS '创建时间';


--
-- TOC entry 4238 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.modify_date IS '更新时间';


--
-- TOC entry 4239 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.creator_id IS '创建人';


--
-- TOC entry 4240 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.modifier_id IS '更新人';


--
-- TOC entry 4241 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.remark IS '备注';


--
-- TOC entry 4242 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.pass_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.pass_status IS '人员是否通过审核
0：是
1：否 字典YES_OR_NO';


--
-- TOC entry 4243 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.pass_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.pass_time IS '审核通过时间';


--
-- TOC entry 4244 (class 0 OID 0)
-- Dependencies: 248
-- Name: COLUMN mm_order_user.order_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_order_user.order_id IS '工单id';


--
-- TOC entry 281 (class 1259 OID 379560)
-- Name: mm_pod_detail; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_pod_detail (
    id numeric(20,0) NOT NULL,
    pod_name character varying(100),
    model_id numeric(20,0),
    model_name character varying(100),
    pod_ip character varying(100),
    status character varying(20),
    host_name character varying(100),
    host_type character varying(100),
    host_ip character varying(100),
    run_time character varying(20),
    project_space_id numeric(20,0),
    project_space_name character varying(100),
    cluster_zone character varying(20),
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    remark character varying
);


--
-- TOC entry 4245 (class 0 OID 0)
-- Dependencies: 281
-- Name: TABLE mm_pod_detail; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_pod_detail IS 'pod详情信息';


--
-- TOC entry 4246 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.id IS '主键';


--
-- TOC entry 4247 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.pod_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.pod_name IS 'pod名称';


--
-- TOC entry 4248 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.model_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.model_id IS '模型id';


--
-- TOC entry 4249 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.model_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.model_name IS '模型名称';


--
-- TOC entry 4250 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.pod_ip; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.pod_ip IS '所在ip';


--
-- TOC entry 4251 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.status IS 'pod状态';


--
-- TOC entry 4252 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.host_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.host_name IS '主机名';


--
-- TOC entry 4253 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.host_type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.host_type IS '主机类型';


--
-- TOC entry 4254 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.host_ip; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.host_ip IS '主机ip';


--
-- TOC entry 4255 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.run_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.run_time IS '使用时长';


--
-- TOC entry 4256 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.project_space_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.project_space_id IS '项目id';


--
-- TOC entry 4257 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.project_space_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.project_space_name IS '项目名';


--
-- TOC entry 4258 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.cluster_zone; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.cluster_zone IS '集群名';


--
-- TOC entry 4259 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.create_date IS '创建时间';


--
-- TOC entry 4260 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.update_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.update_date IS '更新时间';


--
-- TOC entry 4261 (class 0 OID 0)
-- Dependencies: 281
-- Name: COLUMN mm_pod_detail.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pod_detail.remark IS '备注';


--
-- TOC entry 249 (class 1259 OID 306804)
-- Name: mm_pr_test_set_evaluation; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_pr_test_set_evaluation (
    id numeric(20,0) NOT NULL,
    data_set_id numeric(20,0),
    model_task_id numeric(20,0),
    status character varying(20),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    result text,
    deploy_status character varying,
    deploy_target character varying,
    deploy_url character varying(128),
    deploy_finish_date timestamp without time zone,
    type character varying(8),
    is_built boolean DEFAULT false NOT NULL,
    temperature numeric(5,2),
    max_tokens numeric(8,0),
    send_status character varying(16) DEFAULT '1'::character varying
);


--
-- TOC entry 4262 (class 0 OID 0)
-- Dependencies: 249
-- Name: TABLE mm_pr_test_set_evaluation; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_pr_test_set_evaluation IS '问答对测试数据集评估';


--
-- TOC entry 4263 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.id IS '主键';


--
-- TOC entry 4264 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.data_set_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.data_set_id IS '数据集ID';


--
-- TOC entry 4265 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.model_task_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.model_task_id IS '模型任务ID';


--
-- TOC entry 4266 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.status IS '评估状态';


--
-- TOC entry 4267 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.creator_id IS '创建人';


--
-- TOC entry 4268 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.create_date IS '创建时间';


--
-- TOC entry 4269 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.modifier_id IS '更新人';


--
-- TOC entry 4270 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.modify_date IS '更新时间';


--
-- TOC entry 4271 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.result; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.result IS '状态原因';


--
-- TOC entry 4272 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.deploy_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.deploy_status IS '部署状态';


--
-- TOC entry 4273 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.deploy_target; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.deploy_target IS '部署位置,mate.mm_cluster.code';


--
-- TOC entry 4274 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.deploy_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.deploy_url IS '部署任务推理url';


--
-- TOC entry 4275 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.deploy_finish_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.deploy_finish_date IS '部署完成时间';


--
-- TOC entry 4276 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.type IS '评估类型(字典:EVALUATION_TYPE; 0强化; 1普通)';


--
-- TOC entry 4277 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.is_built; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.is_built IS '是否构建强化学习';


--
-- TOC entry 4278 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.temperature; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.temperature IS '温度';


--
-- TOC entry 4279 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.max_tokens; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.max_tokens IS '最大token数';


--
-- TOC entry 4280 (class 0 OID 0)
-- Dependencies: 249
-- Name: COLUMN mm_pr_test_set_evaluation.send_status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation.send_status IS '是否发送给任务0：是 1：否';


--
-- TOC entry 250 (class 1259 OID 306812)
-- Name: mm_pr_test_set_evaluation_detail; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_pr_test_set_evaluation_detail (
    id numeric(20,0) NOT NULL,
    test_set_evaluation_id numeric(20,0),
    reasoning_response text,
    reasoning_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    prompt_response_detail_id numeric(20,0),
    reasoning_two_response text,
    reasoning_two_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4281 (class 0 OID 0)
-- Dependencies: 250
-- Name: TABLE mm_pr_test_set_evaluation_detail; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_pr_test_set_evaluation_detail IS '问答对测试数据集评估详情';


--
-- TOC entry 4282 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.id IS '主键';


--
-- TOC entry 4283 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.test_set_evaluation_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.test_set_evaluation_id IS '测试数据集评估ID';


--
-- TOC entry 4284 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.reasoning_response; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.reasoning_response IS '推理答案';


--
-- TOC entry 4285 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.reasoning_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.reasoning_date IS '推理时间';


--
-- TOC entry 4286 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.prompt_response_detail_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.prompt_response_detail_id IS '问答对详情ID(mm_prompt_response_detail.id)';


--
-- TOC entry 4287 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.reasoning_two_response; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.reasoning_two_response IS '推理答案2';


--
-- TOC entry 4288 (class 0 OID 0)
-- Dependencies: 250
-- Name: COLUMN mm_pr_test_set_evaluation_detail.reasoning_two_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_detail.reasoning_two_date IS '推理时间2';


--
-- TOC entry 251 (class 1259 OID 306819)
-- Name: mm_pr_test_set_evaluation_score; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_pr_test_set_evaluation_score (
    id numeric(20,0) NOT NULL,
    evaluation_detail_id numeric(20,0),
    user_id numeric(20,0),
    action integer,
    action_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4289 (class 0 OID 0)
-- Dependencies: 251
-- Name: TABLE mm_pr_test_set_evaluation_score; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_pr_test_set_evaluation_score IS '问答对测试数据集评估赞踩';


--
-- TOC entry 4290 (class 0 OID 0)
-- Dependencies: 251
-- Name: COLUMN mm_pr_test_set_evaluation_score.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_score.id IS '主键';


--
-- TOC entry 4291 (class 0 OID 0)
-- Dependencies: 251
-- Name: COLUMN mm_pr_test_set_evaluation_score.evaluation_detail_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_score.evaluation_detail_id IS '问答对测试集详情ID';


--
-- TOC entry 4292 (class 0 OID 0)
-- Dependencies: 251
-- Name: COLUMN mm_pr_test_set_evaluation_score.user_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_score.user_id IS '操作人ID';


--
-- TOC entry 4293 (class 0 OID 0)
-- Dependencies: 251
-- Name: COLUMN mm_pr_test_set_evaluation_score.action; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_score.action IS '操作(1赞；-1踩)';


--
-- TOC entry 4294 (class 0 OID 0)
-- Dependencies: 251
-- Name: COLUMN mm_pr_test_set_evaluation_score.action_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_pr_test_set_evaluation_score.action_date IS '操作时间';


--
-- TOC entry 280 (class 1259 OID 379548)
-- Name: mm_project_host_rel; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_project_host_rel (
    id numeric(20,0) NOT NULL,
    project_id numeric(20,0),
    host_name character varying(100),
    create_date timestamp without time zone,
    update_date timestamp without time zone,
    remark character varying
);


--
-- TOC entry 4295 (class 0 OID 0)
-- Dependencies: 280
-- Name: TABLE mm_project_host_rel; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_project_host_rel IS '项目和主机关系表';


--
-- TOC entry 4296 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.id IS 'id';


--
-- TOC entry 4297 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.project_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.project_id IS '项目id';


--
-- TOC entry 4298 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.host_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.host_name IS '主机名';


--
-- TOC entry 4299 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.create_date IS '创建时间';


--
-- TOC entry 4300 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.update_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.update_date IS '更新时间';


--
-- TOC entry 4301 (class 0 OID 0)
-- Dependencies: 280
-- Name: COLUMN mm_project_host_rel.remark; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_host_rel.remark IS '备注';


--
-- TOC entry 275 (class 1259 OID 327394)
-- Name: mm_project_space; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_project_space (
    project_id integer NOT NULL,
    project_name character varying(255) NOT NULL,
    project_region character varying(64) NOT NULL,
    project_leader character varying(64),
    project_label character varying(255),
    status character varying(16) NOT NULL,
    create_by character varying(64) NOT NULL,
    create_time date NOT NULL,
    modify_by character varying(64),
    modify_time date,
    description text
);


--
-- TOC entry 4302 (class 0 OID 0)
-- Dependencies: 275
-- Name: TABLE mm_project_space; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_project_space IS '项目空间信息表';


--
-- TOC entry 4303 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.project_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.project_id IS '项目ID';


--
-- TOC entry 4304 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.project_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.project_name IS '项目名称';


--
-- TOC entry 4305 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.project_region; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.project_region IS '项目所属区域';


--
-- TOC entry 4306 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.project_leader; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.project_leader IS '项目负责人';


--
-- TOC entry 4307 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.project_label; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.project_label IS '项目分类标签';


--
-- TOC entry 4308 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.status IS '项目状态';


--
-- TOC entry 4309 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.create_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.create_by IS '创建人';


--
-- TOC entry 4310 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.create_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.create_time IS '创建时间';


--
-- TOC entry 4311 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.modify_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.modify_by IS '最后修改人';


--
-- TOC entry 4312 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.modify_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.modify_time IS '最后修改时间';


--
-- TOC entry 4313 (class 0 OID 0)
-- Dependencies: 275
-- Name: COLUMN mm_project_space.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_space.description IS '项目详细描述';


--
-- TOC entry 274 (class 1259 OID 327393)
-- Name: mm_project_space_project_id_seq; Type: SEQUENCE; Schema: meta; Owner: -
--

CREATE SEQUENCE meta.mm_project_space_project_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 4314 (class 0 OID 0)
-- Dependencies: 274
-- Name: mm_project_space_project_id_seq; Type: SEQUENCE OWNED BY; Schema: meta; Owner: -
--

ALTER SEQUENCE meta.mm_project_space_project_id_seq OWNED BY meta.mm_project_space.project_id;


--
-- TOC entry 276 (class 1259 OID 327402)
-- Name: mm_project_user; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_project_user (
    project_id integer NOT NULL,
    user_id numeric NOT NULL,
    status character varying(16) NOT NULL,
    create_by character varying(64) NOT NULL,
    create_time date NOT NULL,
    modify_by character varying(64),
    modify_time date,
    description text
);


--
-- TOC entry 4315 (class 0 OID 0)
-- Dependencies: 276
-- Name: TABLE mm_project_user; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_project_user IS '项目空间成员表';


--
-- TOC entry 4316 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.project_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.project_id IS '项目ID';


--
-- TOC entry 4317 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.user_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.user_id IS '用户ID';


--
-- TOC entry 4318 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.status IS '项目成员状态';


--
-- TOC entry 4319 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.create_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.create_by IS '创建人';


--
-- TOC entry 4320 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.create_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.create_time IS '创建时间';


--
-- TOC entry 4321 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.modify_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.modify_by IS '最后修改人';


--
-- TOC entry 4322 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.modify_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.modify_time IS '最后修改时间';


--
-- TOC entry 4323 (class 0 OID 0)
-- Dependencies: 276
-- Name: COLUMN mm_project_user.description; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user.description IS '项目成员详细描述';


--
-- TOC entry 277 (class 1259 OID 327409)
-- Name: mm_project_user_role; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_project_user_role (
    project_id integer NOT NULL,
    user_id numeric NOT NULL,
    role_id numeric NOT NULL,
    status character varying(16) NOT NULL,
    create_by character varying(64) NOT NULL,
    create_time date NOT NULL,
    modify_by character varying(64),
    modify_time date
);


--
-- TOC entry 4324 (class 0 OID 0)
-- Dependencies: 277
-- Name: TABLE mm_project_user_role; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_project_user_role IS '项目空间成员角色表';


--
-- TOC entry 4325 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.project_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.project_id IS '项目ID';


--
-- TOC entry 4326 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.user_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.user_id IS '用户ID';


--
-- TOC entry 4327 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.role_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.role_id IS '角色ID';


--
-- TOC entry 4328 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.status IS '项目成员状态';


--
-- TOC entry 4329 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.create_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.create_by IS '创建人';


--
-- TOC entry 4330 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.create_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.create_time IS '创建时间';


--
-- TOC entry 4331 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.modify_by; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.modify_by IS '最后修改人';


--
-- TOC entry 4332 (class 0 OID 0)
-- Dependencies: 277
-- Name: COLUMN mm_project_user_role.modify_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_project_user_role.modify_time IS '最后修改时间';


--
-- TOC entry 252 (class 1259 OID 306823)
-- Name: mm_prompt_category_detail; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompt_category_detail (
    id numeric(20,0) NOT NULL,
    data_set_file_id numeric(20,0),
    question_role character varying(64),
    prompt text,
    category character varying(64),
    status character varying(8) DEFAULT 1,
    param1 character varying(255),
    param2 character varying(255),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4333 (class 0 OID 0)
-- Dependencies: 252
-- Name: TABLE mm_prompt_category_detail; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_prompt_category_detail IS '意图识别数据集详情';


--
-- TOC entry 4334 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.id IS '主键';


--
-- TOC entry 4335 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.data_set_file_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.data_set_file_id IS '数据集文件ID';


--
-- TOC entry 4336 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.question_role; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.question_role IS '提问角色';


--
-- TOC entry 4337 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.prompt; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.prompt IS '问题内容';


--
-- TOC entry 4338 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.category; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.category IS '分类';


--
-- TOC entry 4339 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.status IS '状态';


--
-- TOC entry 4340 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.param1; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.param1 IS '备用字段1';


--
-- TOC entry 4341 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.param2; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.param2 IS '备用字段2';


--
-- TOC entry 4342 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.creator_id IS '创建人';


--
-- TOC entry 4343 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.create_date IS '创建时间';


--
-- TOC entry 4344 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.modifier_id IS '更新人';


--
-- TOC entry 4345 (class 0 OID 0)
-- Dependencies: 252
-- Name: COLUMN mm_prompt_category_detail.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_category_detail.modify_date IS '更新时间';


--
-- TOC entry 253 (class 1259 OID 306831)
-- Name: mm_prompt_resp; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompt_resp (
    id numeric(20,0) NOT NULL,
    data_set_id numeric(20,0),
    context text,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_valid character varying(8) DEFAULT 10050001,
    is_delete character varying(8) DEFAULT 10050002
);


--
-- TOC entry 4346 (class 0 OID 0)
-- Dependencies: 253
-- Name: TABLE mm_prompt_resp; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_prompt_resp IS '问答详情';


--
-- TOC entry 4347 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.id IS 'id;id';


--
-- TOC entry 4348 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.data_set_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.data_set_id IS '数据集id';


--
-- TOC entry 4349 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.context; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.context IS '内容';


--
-- TOC entry 4350 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.creator_id IS '创建人id;1';


--
-- TOC entry 4351 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.create_date IS '创建时间';


--
-- TOC entry 4352 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.modifier_id IS '修改人id';


--
-- TOC entry 4353 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.modify_date IS '修改时间';


--
-- TOC entry 4354 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.is_valid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.is_valid IS '是否有效';


--
-- TOC entry 4355 (class 0 OID 0)
-- Dependencies: 253
-- Name: COLUMN mm_prompt_resp.is_delete; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_resp.is_delete IS '是否删除';


--
-- TOC entry 254 (class 1259 OID 306840)
-- Name: mm_prompt_response_detail; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompt_response_detail (
    id numeric(20,0) NOT NULL,
    data_set_file_id numeric(20,0),
    question_id integer,
    rank integer,
    prompt text,
    response text,
    status character varying(8) DEFAULT 1,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- TOC entry 4356 (class 0 OID 0)
-- Dependencies: 254
-- Name: TABLE mm_prompt_response_detail; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_prompt_response_detail IS '问答对详情';


--
-- TOC entry 4357 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.id IS '主键';


--
-- TOC entry 4358 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.data_set_file_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.data_set_file_id IS '数据集文件ID';


--
-- TOC entry 4359 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.question_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.question_id IS '问题id';


--
-- TOC entry 4360 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.rank; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.rank IS '对话轮次';


--
-- TOC entry 4361 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.prompt; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.prompt IS '问题';


--
-- TOC entry 4362 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.response; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.response IS '回答';


--
-- TOC entry 4363 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.status IS '状态(0异常; 1正常)';


--
-- TOC entry 4364 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.creator_id IS '创建人';


--
-- TOC entry 4365 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.create_date IS '创建时间';


--
-- TOC entry 4366 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.modifier_id IS '更新人';


--
-- TOC entry 4367 (class 0 OID 0)
-- Dependencies: 254
-- Name: COLUMN mm_prompt_response_detail.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_response_detail.modify_date IS '更新时间';


--
-- TOC entry 255 (class 1259 OID 306848)
-- Name: mm_prompt_sequential_detail; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompt_sequential_detail (
    id numeric(20,0) NOT NULL,
    data_set_file_id numeric(20,0),
    circuit_id character varying(255),
    cir_name character varying(255),
    k_dev_id character varying(255),
    k_intf_id character varying(255),
    devid_ip character varying(255),
    b_dev_id character varying(255),
    b_intf_descr character varying(255),
    b_devid_ip character varying(255),
    d_cirbw character varying(255),
    d_in_flux character varying(255),
    d_out_flux character varying(255),
    d_in_flux_ratio character varying(255),
    d_out_flux_ratio character varying(255),
    t_ctime timestamp without time zone,
    status character varying(8),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    param0 character varying(255),
    param1 character varying(255),
    param2 character varying(255),
    param3 character varying(255),
    param4 character varying(255),
    param5 character varying(255)
);


--
-- TOC entry 4368 (class 0 OID 0)
-- Dependencies: 255
-- Name: TABLE mm_prompt_sequential_detail; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_prompt_sequential_detail IS '时序测试数据集详情';


--
-- TOC entry 4369 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.id IS '主键';


--
-- TOC entry 4370 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.data_set_file_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.data_set_file_id IS '模型ID';


--
-- TOC entry 4371 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.circuit_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.circuit_id IS '链路编号';


--
-- TOC entry 4372 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.cir_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.cir_name IS '电路名称';


--
-- TOC entry 4373 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.k_dev_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.k_dev_id IS 'A端设备编码';


--
-- TOC entry 4374 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.k_intf_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.k_intf_id IS 'A端端口';


--
-- TOC entry 4375 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.devid_ip; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.devid_ip IS 'A端设备IP';


--
-- TOC entry 4376 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.b_dev_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.b_dev_id IS 'b端设备编码';


--
-- TOC entry 4377 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.b_intf_descr; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.b_intf_descr IS 'b端端口号';


--
-- TOC entry 4378 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.b_devid_ip; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.b_devid_ip IS 'b端设备IP';


--
-- TOC entry 4379 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.d_cirbw; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.d_cirbw IS '带宽';


--
-- TOC entry 4380 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.d_in_flux; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.d_in_flux IS '流入流速';


--
-- TOC entry 4381 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.d_out_flux; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.d_out_flux IS '流出流速';


--
-- TOC entry 4382 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.d_in_flux_ratio; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.d_in_flux_ratio IS '流入带宽利用率';


--
-- TOC entry 4383 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.d_out_flux_ratio; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.d_out_flux_ratio IS '流出带宽利用率';


--
-- TOC entry 4384 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.t_ctime; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.t_ctime IS '采集时间';


--
-- TOC entry 4385 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.status; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.status IS '状态';


--
-- TOC entry 4386 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.creator_id IS '创建人';


--
-- TOC entry 4387 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.create_date IS '创建时间';


--
-- TOC entry 4388 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.modifier_id IS '更新人';


--
-- TOC entry 4389 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.modify_date IS '更新时间';


--
-- TOC entry 4390 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param0; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param0 IS '保留字段';


--
-- TOC entry 4391 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param1; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param1 IS '保留字段';


--
-- TOC entry 4392 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param2; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param2 IS '保留字段';


--
-- TOC entry 4393 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param3; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param3 IS '保留字段';


--
-- TOC entry 4394 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param4; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param4 IS '保留字段';


--
-- TOC entry 4395 (class 0 OID 0)
-- Dependencies: 255
-- Name: COLUMN mm_prompt_sequential_detail.param5; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompt_sequential_detail.param5 IS '保留字段';


--
-- TOC entry 256 (class 1259 OID 306855)
-- Name: mm_prompt_templates; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompt_templates (
    id numeric(20,0) NOT NULL,
    template_name character varying(255) NOT NULL,
    template_text text NOT NULL,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    description text
);


--
-- TOC entry 257 (class 1259 OID 306862)
-- Name: mm_prompts; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_prompts (
    id numeric(20,0) NOT NULL,
    prompt_text text NOT NULL,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    model_id numeric(20,0),
    name character varying(64) NOT NULL,
    identifier character varying(16),
    type character varying(64) NOT NULL,
    belong character varying(8) NOT NULL,
    variable character varying(128),
    int_url character varying(128),
    region_code character varying(64)
);


--
-- TOC entry 4396 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.id IS '主键';


--
-- TOC entry 4397 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.prompt_text; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.prompt_text IS '提示词内容';


--
-- TOC entry 4398 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.creator_id IS '创建人';


--
-- TOC entry 4399 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.create_date IS '创建时间';


--
-- TOC entry 4400 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.modifier_id IS '更新人';


--
-- TOC entry 4401 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.modify_date IS '更新时间';


--
-- TOC entry 4402 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.model_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.model_id IS '模板ID,mm_prompt_templates.id,-1时为未绑定模版';


--
-- TOC entry 4403 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.name IS '名称';


--
-- TOC entry 4404 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.identifier; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.identifier IS '变量标识符,详见字典PROMPT_IDENTIFIER';


--
-- TOC entry 4405 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.type; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.type IS '类别，详见字典PROMPT_TYPE。-1为自定义缺省类型';


--
-- TOC entry 4406 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.belong IS '归属，详见字典PROMPT_BELONG';


--
-- TOC entry 4407 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.variable; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.variable IS '变量名称';


--
-- TOC entry 4408 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.int_url; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.int_url IS '接口信息';


--
-- TOC entry 4409 (class 0 OID 0)
-- Dependencies: 257
-- Name: COLUMN mm_prompts.region_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_prompts.region_code IS '区域编码';


--
-- TOC entry 258 (class 1259 OID 306869)
-- Name: mm_province; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_province (
    id numeric(20,0) NOT NULL,
    name character varying(64) NOT NULL,
    code character varying(64),
    abbreviation character varying(16)
);


--
-- TOC entry 4410 (class 0 OID 0)
-- Dependencies: 258
-- Name: TABLE mm_province; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_province IS '省份表';


--
-- TOC entry 4411 (class 0 OID 0)
-- Dependencies: 258
-- Name: COLUMN mm_province.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_province.id IS '主键';


--
-- TOC entry 4412 (class 0 OID 0)
-- Dependencies: 258
-- Name: COLUMN mm_province.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_province.name IS '名称';


--
-- TOC entry 4413 (class 0 OID 0)
-- Dependencies: 258
-- Name: COLUMN mm_province.code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_province.code IS '编码';


--
-- TOC entry 4414 (class 0 OID 0)
-- Dependencies: 258
-- Name: COLUMN mm_province.abbreviation; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_province.abbreviation IS '省份缩写';


--
-- TOC entry 259 (class 1259 OID 306872)
-- Name: mm_user; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_user (
    id numeric(20,0) NOT NULL,
    employee_number character varying(64) NOT NULL,
    name character varying(64) NOT NULL,
    user_name character varying(64) NOT NULL,
    region_code character varying(64),
    region_name character varying(64),
    major_code character varying(255),
    major_name character varying(255),
    mobile character varying(16),
    email character varying(64),
    dept_name character varying(64),
    corp_name character varying(255),
    is_valid numeric(8,0) DEFAULT 0,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp(0) without time zone NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp(0) without time zone NOT NULL,
    last_active_time timestamp(0) without time zone,
    role character varying(4) DEFAULT '3'::character varying,
    tool_auth character varying(8) DEFAULT '1'::character varying,
    agent_auth character varying(8) DEFAULT '1'::character varying,
    system_auth character varying(8) DEFAULT '1'::character varying,
    group_branch character varying(128),
    belong character varying(8)
);


--
-- TOC entry 4415 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.id IS 'ID';


--
-- TOC entry 4416 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.employee_number; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.employee_number IS '中国电信人力编号';


--
-- TOC entry 4417 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.name IS '真实姓名';


--
-- TOC entry 4418 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.user_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.user_name IS '用户名';


--
-- TOC entry 4419 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.region_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.region_code IS '区域编码';


--
-- TOC entry 4420 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.region_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.region_name IS '区域名称';


--
-- TOC entry 4421 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.major_code; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.major_code IS '权限编码';


--
-- TOC entry 4422 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.major_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.major_name IS '权限';


--
-- TOC entry 4423 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.mobile; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.mobile IS '手机号';


--
-- TOC entry 4424 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.email; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.email IS '邮箱';


--
-- TOC entry 4425 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.dept_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.dept_name IS '部门名称';


--
-- TOC entry 4426 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.corp_name; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.corp_name IS '公司名称';


--
-- TOC entry 4427 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.is_valid; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.is_valid IS '是否合理';


--
-- TOC entry 4428 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.creator_id IS '创建人';


--
-- TOC entry 4429 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.create_date IS '创建时间';


--
-- TOC entry 4430 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.modifier_id IS '修改人';


--
-- TOC entry 4431 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.modify_date IS '修改时间';


--
-- TOC entry 4432 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.last_active_time; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.last_active_time IS '最新活动时间';


--
-- TOC entry 4433 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.role; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.role IS '用户角色，字典USER_ROLE';


--
-- TOC entry 4434 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.tool_auth; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.tool_auth IS '是否有工具链平台权限,字典YSE_OR_NO';


--
-- TOC entry 4435 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.agent_auth; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.agent_auth IS '是否有智能体开放平台权限,字典YSE_OR_NO';


--
-- TOC entry 4436 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.system_auth; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.system_auth IS '是否有系统管理权限,字典YSE_OR_NO';


--
-- TOC entry 4437 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.group_branch; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.group_branch IS '集团下的分公司名称，由corp_name解析二级获得';


--
-- TOC entry 4438 (class 0 OID 0)
-- Dependencies: 259
-- Name: COLUMN mm_user.belong; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user.belong IS '用户归属（人力编号后2位）';


--
-- TOC entry 260 (class 1259 OID 306882)
-- Name: mm_user_model; Type: TABLE; Schema: meta; Owner: -
--

CREATE TABLE meta.mm_user_model (
    id numeric(20,0) NOT NULL,
    user_id numeric(20,0),
    model_id numeric(20,0),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    usage character varying(8)
);


--
-- TOC entry 4439 (class 0 OID 0)
-- Dependencies: 260
-- Name: TABLE mm_user_model; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON TABLE meta.mm_user_model IS '用户模型关系表';


--
-- TOC entry 4440 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.id IS 'ID';


--
-- TOC entry 4441 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.user_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.user_id IS '用户ID';


--
-- TOC entry 4442 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.model_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.model_id IS '模型ID';


--
-- TOC entry 4443 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.creator_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.creator_id IS '创建人';


--
-- TOC entry 4444 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.create_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.create_date IS '创建时间';


--
-- TOC entry 4445 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.modifier_id; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.modifier_id IS '更新人';


--
-- TOC entry 4446 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.modify_date; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.modify_date IS '更新时间';


--
-- TOC entry 4447 (class 0 OID 0)
-- Dependencies: 260
-- Name: COLUMN mm_user_model.usage; Type: COMMENT; Schema: meta; Owner: -
--

COMMENT ON COLUMN meta.mm_user_model.usage IS '模型权限用途，字典MODEL_AUTH_USAGE';


--
-- TOC entry 261 (class 1259 OID 306887)
-- Name: mm_api_id_seq; Type: SEQUENCE; Schema: register; Owner: -
--

CREATE SEQUENCE register.mm_api_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 9999999
    CACHE 1;


--
-- TOC entry 262 (class 1259 OID 306888)
-- Name: mm_dcoos_api; Type: TABLE; Schema: register; Owner: -
--

CREATE TABLE register.mm_dcoos_api (
    id character varying NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    method_type character varying(64) NOT NULL,
    api_url character varying(255) NOT NULL,
    request_params text,
    response_params text,
    status character varying(64) DEFAULT 1,
    creator_id character varying(32),
    create_date timestamp without time zone,
    modifier_id character varying(32),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    is_valid character varying DEFAULT 10050001 NOT NULL,
    is_delete character varying DEFAULT 10050002 NOT NULL,
    service_type character varying(64),
    dcoss_api_url character varying(255)
);


--
-- TOC entry 4448 (class 0 OID 0)
-- Dependencies: 262
-- Name: COLUMN mm_dcoos_api.status; Type: COMMENT; Schema: register; Owner: -
--

COMMENT ON COLUMN register.mm_dcoos_api.status IS '״̬';


--
-- TOC entry 263 (class 1259 OID 306897)
-- Name: mm_dcoos_api_template; Type: TABLE; Schema: register; Owner: -
--

CREATE TABLE register.mm_dcoos_api_template (
    id numeric(20,0) NOT NULL,
    name character varying(255),
    code character varying(64) NOT NULL,
    body text,
    response_body text,
    is_valid character varying(8) DEFAULT 10050001 NOT NULL,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_delete character varying(8) DEFAULT 10050002
);


--
-- TOC entry 4449 (class 0 OID 0)
-- Dependencies: 263
-- Name: COLUMN mm_dcoos_api_template.id; Type: COMMENT; Schema: register; Owner: -
--

COMMENT ON COLUMN register.mm_dcoos_api_template.id IS 'id';


--
-- TOC entry 264 (class 1259 OID 306905)
-- Name: mm_area; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_area (
    id integer NOT NULL,
    name character varying(64),
    parent_id integer,
    short_name character varying(32),
    level_type character varying(1),
    city_code character varying(8),
    zip_code character varying(8),
    longitude character varying(16),
    latitude character varying(16),
    pinyin character varying(128),
    status character varying(1)
);


--
-- TOC entry 4450 (class 0 OID 0)
-- Dependencies: 264
-- Name: TABLE mm_area; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_area IS '地区';


--
-- TOC entry 4451 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.id IS '主键';


--
-- TOC entry 4452 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.name IS '省市区名称';


--
-- TOC entry 4453 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.parent_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.parent_id IS '上级ID';


--
-- TOC entry 4454 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.short_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.short_name IS '简称';


--
-- TOC entry 4455 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.level_type; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.level_type IS '级别:0,中国；1，省分；2，市；3，区、县';


--
-- TOC entry 4456 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.city_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.city_code IS '城市代码';


--
-- TOC entry 4457 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.zip_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.zip_code IS '邮编';


--
-- TOC entry 4458 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.longitude; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.longitude IS '经度';


--
-- TOC entry 4459 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.latitude; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.latitude IS '纬度';


--
-- TOC entry 4460 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.pinyin; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.pinyin IS '拼音';


--
-- TOC entry 4461 (class 0 OID 0)
-- Dependencies: 264
-- Name: COLUMN mm_area.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_area.status IS '状态(0异常; 1正常)';


--
-- TOC entry 265 (class 1259 OID 306908)
-- Name: mm_deploy_area; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_deploy_area (
    id numeric(20,0) NOT NULL,
    area character varying(32),
    status character varying(1) DEFAULT 1,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 4462 (class 0 OID 0)
-- Dependencies: 265
-- Name: TABLE mm_deploy_area; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_deploy_area IS '部署地区';


--
-- TOC entry 4463 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.id IS '主键';


--
-- TOC entry 4464 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.area; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.area IS '地区(西南1,西南2...)';


--
-- TOC entry 4465 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.status IS '部署状态';


--
-- TOC entry 4466 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.creator_id IS '创建人';


--
-- TOC entry 4467 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.create_date IS '创建时间';


--
-- TOC entry 4468 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.modifier_id IS '更新人';


--
-- TOC entry 4469 (class 0 OID 0)
-- Dependencies: 265
-- Name: COLUMN mm_deploy_area.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_area.modify_date IS '更新时间';


--
-- TOC entry 266 (class 1259 OID 306914)
-- Name: mm_deploy_server; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_deploy_server (
    id numeric(20,0) NOT NULL,
    code character varying(64),
    name character varying(64) NOT NULL,
    area1 numeric(20,0) NOT NULL,
    area2 numeric(20,0),
    area_name character varying(32) NOT NULL,
    ip character varying(32) NOT NULL,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status character varying DEFAULT 1 NOT NULL
);


--
-- TOC entry 4470 (class 0 OID 0)
-- Dependencies: 266
-- Name: TABLE mm_deploy_server; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_deploy_server IS '部署服务器信息';


--
-- TOC entry 4471 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.id IS '主键';


--
-- TOC entry 4472 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.code IS '服务器编码';


--
-- TOC entry 4473 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.name IS '服务器名称';


--
-- TOC entry 4474 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.area1; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.area1 IS '一级地区';


--
-- TOC entry 4475 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.area2; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.area2 IS '二级地区';


--
-- TOC entry 4476 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.area_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.area_name IS '地区名称';


--
-- TOC entry 4477 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.ip; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.ip IS '服务器IP';


--
-- TOC entry 4478 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.creator_id IS '创建人';


--
-- TOC entry 4479 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.create_date IS '创建时间';


--
-- TOC entry 4480 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.modifier_id IS '更新人';


--
-- TOC entry 4481 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.modify_date IS '更新时间';


--
-- TOC entry 4482 (class 0 OID 0)
-- Dependencies: 266
-- Name: COLUMN mm_deploy_server.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server.status IS '状态(-1删除; 0禁用; 1正常)';


--
-- TOC entry 267 (class 1259 OID 306922)
-- Name: mm_deploy_server_card; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_deploy_server_card (
    id numeric(20,0) NOT NULL,
    deploy_server_id numeric(20,0) NOT NULL,
    card_name character varying(64) NOT NULL,
    card_code character varying NOT NULL,
    status character varying(1) DEFAULT 1 NOT NULL,
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    index character varying(1)
);


--
-- TOC entry 4483 (class 0 OID 0)
-- Dependencies: 267
-- Name: TABLE mm_deploy_server_card; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_deploy_server_card IS '部署服务器卡信息';


--
-- TOC entry 4484 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.id IS '主键';


--
-- TOC entry 4485 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.deploy_server_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.deploy_server_id IS '服务器编码';


--
-- TOC entry 4486 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.card_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.card_name IS '卡名称';


--
-- TOC entry 4487 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.card_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.card_code IS '卡编码';


--
-- TOC entry 4488 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.status IS '状态(1启用;0禁用)';


--
-- TOC entry 4489 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.creator_id IS '创建人';


--
-- TOC entry 4490 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.create_date IS '创建时间';


--
-- TOC entry 4491 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.modifier_id IS '更新人';


--
-- TOC entry 4492 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.modify_date IS '更新时间';


--
-- TOC entry 4493 (class 0 OID 0)
-- Dependencies: 267
-- Name: COLUMN mm_deploy_server_card.index; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_server_card.index IS '索引';


--
-- TOC entry 268 (class 1259 OID 306930)
-- Name: mm_deploy_task; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_deploy_task (
    id numeric(20,0) NOT NULL,
    model_id numeric(20,0),
    status character varying(16),
    result character varying(128),
    creator_id numeric(20,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(20,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    model_name character varying,
    region_code character varying(64),
    deploy_url character varying,
    agent_status character varying(1) DEFAULT '1'::character varying,
    deploy_target character varying(16),
    submit_status character varying(8) DEFAULT '1'::character varying,
    register_id character varying,
    deploy_type character varying DEFAULT 1,
    register_status character varying(1) DEFAULT '1'::character varying,
    deploy_belong character varying(10),
    project_space_id character varying(50)
);


--
-- TOC entry 4494 (class 0 OID 0)
-- Dependencies: 268
-- Name: TABLE mm_deploy_task; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_deploy_task IS '部署任务';


--
-- TOC entry 4495 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.id IS '主键';


--
-- TOC entry 4496 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.model_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.model_id IS '模型ID,train.mm_train_task.id';


--
-- TOC entry 4497 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.status IS '部署状态';


--
-- TOC entry 4498 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.result; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.result IS '结果信息';


--
-- TOC entry 4499 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.creator_id IS '创建人';


--
-- TOC entry 4500 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.create_date IS '创建时间';


--
-- TOC entry 4501 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.modifier_id IS '更新人';


--
-- TOC entry 4502 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.modify_date IS '更新时间';


--
-- TOC entry 4503 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.model_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.model_name IS '模型名称';


--
-- TOC entry 4504 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.region_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.region_code IS '区域编码';


--
-- TOC entry 4505 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.deploy_url; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.deploy_url IS '模型部署地址';


--
-- TOC entry 4506 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.agent_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.agent_status IS '智能体是否引用，字典YES_OR_NO';


--
-- TOC entry 4507 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.deploy_target; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.deploy_target IS '部署接口目标，mate.mm_cluster.code';


--
-- TOC entry 4508 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.submit_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.submit_status IS '是否已提交到k8s队列，0是，1否 字典YES_OR_NO';


--
-- TOC entry 4509 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.register_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.register_id IS 'dcoos能力注册ID';


--
-- TOC entry 4510 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.deploy_type; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.deploy_type IS '部署类型(1训练; 2测试)';


--
-- TOC entry 4511 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.register_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.register_status IS '模型是否注册, 字典YES_OR_NO';


--
-- TOC entry 4512 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.deploy_belong; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.deploy_belong IS '部署任务归属 1：工具链 2：项目空间';


--
-- TOC entry 4513 (class 0 OID 0)
-- Dependencies: 268
-- Name: COLUMN mm_deploy_task.project_space_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_deploy_task.project_space_id IS '项目空间id';


--
-- TOC entry 279 (class 1259 OID 371339)
-- Name: mm_task_group; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_task_group (
    id numeric(20,0) NOT NULL,
    name character varying(200),
    deploy_status character varying(16),
    region_code character varying(64),
    project_id numeric(20,0),
    model_id numeric(20,0),
    parent_id numeric(20,0),
    train_target character varying(16),
    type character varying(16),
    classify character varying(8),
    creator_id numeric(20,0),
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(20,0),
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- TOC entry 4514 (class 0 OID 0)
-- Dependencies: 279
-- Name: TABLE mm_task_group; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_task_group IS '训练部署任务组表';


--
-- TOC entry 4515 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.id IS 'id';


--
-- TOC entry 4516 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.name IS '任务组名称';


--
-- TOC entry 4517 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.deploy_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.deploy_status IS '部署状态';


--
-- TOC entry 4518 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.region_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.region_code IS '区域编码';


--
-- TOC entry 4519 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.project_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.project_id IS '项目id';


--
-- TOC entry 4520 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.model_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.model_id IS '模型ID，meta.mm_model.id';


--
-- TOC entry 4521 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.parent_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.parent_id IS '父任务ID，train.mm_train_task.id';


--
-- TOC entry 4522 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.train_target; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.train_target IS '训练接口目标，,mate.mm_cluster.code';


--
-- TOC entry 4523 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.type; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.type IS '训练类型，字典MODEL_TRAIN_TYPE';


--
-- TOC entry 4524 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.classify; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.classify IS '训练分类，字典MODEL_TRAIN_CLASSIFY';


--
-- TOC entry 4525 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.creator_id IS '更新时间';


--
-- TOC entry 4526 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.create_date IS '创建时间';


--
-- TOC entry 4527 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.modifier_id IS '更新人';


--
-- TOC entry 4528 (class 0 OID 0)
-- Dependencies: 279
-- Name: COLUMN mm_task_group.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_task_group.modify_date IS '更新时间';


--
-- TOC entry 278 (class 1259 OID 327416)
-- Name: mm_train_dataset_join; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_train_dataset_join (
    id numeric(24,0) NOT NULL,
    task_id numeric(24,0) NOT NULL,
    data_set_id numeric(24,0) NOT NULL
);


--
-- TOC entry 4529 (class 0 OID 0)
-- Dependencies: 278
-- Name: TABLE mm_train_dataset_join; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_train_dataset_join IS '训练任务数据集关联表';


--
-- TOC entry 4530 (class 0 OID 0)
-- Dependencies: 278
-- Name: COLUMN mm_train_dataset_join.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_dataset_join.id IS 'id';


--
-- TOC entry 4531 (class 0 OID 0)
-- Dependencies: 278
-- Name: COLUMN mm_train_dataset_join.task_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_dataset_join.task_id IS '训练任务id';


--
-- TOC entry 4532 (class 0 OID 0)
-- Dependencies: 278
-- Name: COLUMN mm_train_dataset_join.data_set_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_dataset_join.data_set_id IS '数据集id';


--
-- TOC entry 269 (class 1259 OID 306946)
-- Name: mm_train_task; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_train_task (
    id numeric(20,0) NOT NULL,
    name character varying(64) NOT NULL,
    model_id numeric(20,0),
    method character varying(16),
    data_set_id numeric(20,0) NOT NULL,
    param text,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status character varying(16),
    region_name character varying(128),
    iterate_total numeric(16,0),
    iterate_curr numeric(16,0),
    runtime character varying(128),
    remain_time character varying(128),
    result text,
    type character varying(16),
    region_code character varying(64),
    submit_status character varying(8) DEFAULT '1'::character varying,
    training_loss text,
    train_target character varying(16),
    loss_trend text,
    classify character varying(8),
    parent_id numeric(20,0),
    project_id numeric(24,0),
    instruction character varying,
    group_id numeric(20,0),
    version_num numeric(2,0),
    version_enable character varying(2) DEFAULT '1'::character varying
);


--
-- TOC entry 4533 (class 0 OID 0)
-- Dependencies: 269
-- Name: TABLE mm_train_task; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_train_task IS '训练任务表表';


--
-- TOC entry 4534 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.id IS '主键';


--
-- TOC entry 4535 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.name IS '名称';


--
-- TOC entry 4536 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.model_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.model_id IS '模型ID，meta.mm_model.id';


--
-- TOC entry 4537 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.method; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.method IS '训练方法，字典TRAIN_TASK_METHOD';


--
-- TOC entry 4538 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.data_set_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.data_set_id IS '数据集ID,meta.mm_data_set.id';


--
-- TOC entry 4539 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.param; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.param IS '超参配置json';


--
-- TOC entry 4540 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.creator_id IS '创建人';


--
-- TOC entry 4541 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.create_date IS '创建时间';


--
-- TOC entry 4542 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.modifier_id IS '更新人';


--
-- TOC entry 4543 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.modify_date IS '更新时间';


--
-- TOC entry 4544 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.status IS '任务状态，字典TRAIN_TASK_STATUS';


--
-- TOC entry 4545 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.region_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.region_name IS '省份';


--
-- TOC entry 4546 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.iterate_total; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.iterate_total IS '总迭代次数';


--
-- TOC entry 4547 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.iterate_curr; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.iterate_curr IS '当前迭代次数';


--
-- TOC entry 4548 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.runtime; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.runtime IS '已运行时间';


--
-- TOC entry 4549 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.remain_time; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.remain_time IS '预计剩余训练时间';


--
-- TOC entry 4550 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.result; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.result IS '训练结果信息';


--
-- TOC entry 4551 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.type; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.type IS '训练类型，字典MODEL_TRAIN_TYPE';


--
-- TOC entry 4552 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.region_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.region_code IS '区域编码';


--
-- TOC entry 4553 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.submit_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.submit_status IS '是否已提交到k8s队列，0是，1否 字典YES_OR_NO';


--
-- TOC entry 4554 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.training_loss; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.training_loss IS '训练Loss数据(有序loss值按,分隔)';


--
-- TOC entry 4555 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.train_target; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.train_target IS '训练接口目标，,mate.mm_cluster.code';


--
-- TOC entry 4556 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.loss_trend; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.loss_trend IS 'Loss趋势分析';


--
-- TOC entry 4557 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.classify; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.classify IS '训练分类，字典MODEL_TRAIN_CLASSIFY';


--
-- TOC entry 4558 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.parent_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.parent_id IS '父任务ID，train.mm_train_task.id';


--
-- TOC entry 4559 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.project_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.project_id IS '项目id';


--
-- TOC entry 4560 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.instruction; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.instruction IS '提示词';


--
-- TOC entry 4561 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.group_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.group_id IS '任务组id';


--
-- TOC entry 4562 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.version_num; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.version_num IS '版本号';


--
-- TOC entry 4563 (class 0 OID 0)
-- Dependencies: 269
-- Name: COLUMN mm_train_task.version_enable; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task.version_enable IS '版本是否启用 0:启用 1:未启用';


--
-- TOC entry 270 (class 1259 OID 306954)
-- Name: mm_train_task_demo; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_train_task_demo (
    id numeric(20,0) NOT NULL,
    name character varying(64) NOT NULL,
    model_id numeric(20,0),
    method character varying(16),
    data_set_id numeric(20,0) NOT NULL,
    param text,
    creator_id numeric(24,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(24,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status character varying(16),
    region_name character varying(128),
    iterate_total numeric(16,0),
    iterate_curr numeric(16,0),
    runtime character varying(128),
    remain_time character varying(128),
    result text,
    type character varying(16),
    region_code character varying(64),
    training_loss text,
    submit_status character varying(8) DEFAULT '1'::character varying,
    train_target character varying(16),
    loss_trend text,
    ai_cluster_id character varying,
    ai_cluster_name character varying(128),
    order_id character varying(100),
    plan_id character varying(100),
    candidate_id character varying(100)
);


--
-- TOC entry 4564 (class 0 OID 0)
-- Dependencies: 270
-- Name: TABLE mm_train_task_demo; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_train_task_demo IS '训练任务表表';


--
-- TOC entry 4565 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.id IS '主键';


--
-- TOC entry 4566 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.name IS '名称';


--
-- TOC entry 4567 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.model_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.model_id IS '模型ID，meta.mm_model.id';


--
-- TOC entry 4568 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.method; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.method IS '训练方法，字典TRAIN_TASK_METHOD';


--
-- TOC entry 4569 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.data_set_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.data_set_id IS '数据集ID,meta.mm_data_set.id';


--
-- TOC entry 4570 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.param; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.param IS '超参配置json';


--
-- TOC entry 4571 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.creator_id IS '创建人';


--
-- TOC entry 4572 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.create_date IS '创建时间';


--
-- TOC entry 4573 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.modifier_id IS '更新人';


--
-- TOC entry 4574 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.modify_date IS '更新时间';


--
-- TOC entry 4575 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.status IS '任务状态，字典TRAIN_TASK_STATUS';


--
-- TOC entry 4576 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.region_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.region_name IS '省份';


--
-- TOC entry 4577 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.iterate_total; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.iterate_total IS '总迭代次数';


--
-- TOC entry 4578 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.iterate_curr; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.iterate_curr IS '当前迭代次数';


--
-- TOC entry 4579 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.runtime; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.runtime IS '已运行时间';


--
-- TOC entry 4580 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.remain_time; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.remain_time IS '预计剩余训练时间';


--
-- TOC entry 4581 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.result; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.result IS '训练结果信息';


--
-- TOC entry 4582 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.type; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.type IS '训练类型，字典MODEL_TRAIN_TYPE';


--
-- TOC entry 4583 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.region_code; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.region_code IS '区域编码';


--
-- TOC entry 4584 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.training_loss; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.training_loss IS '训练Loss数据(有序loss值按,分隔)';


--
-- TOC entry 4585 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.submit_status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.submit_status IS '是否已提交到k8s队列，0是，1否 字典YES_OR_NO';


--
-- TOC entry 4586 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.train_target; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.train_target IS '训练接口目标,mate.mm_cluster.code';


--
-- TOC entry 4587 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.loss_trend; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.loss_trend IS 'Loss趋势分析';


--
-- TOC entry 4588 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.ai_cluster_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.ai_cluster_id IS '集群ID，数据来源 算网调度AI-智算平台接口，id字段';


--
-- TOC entry 4589 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.ai_cluster_name; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.ai_cluster_name IS '集群名称，数据来源 算网调度AI-智算平台接口,title字段';


--
-- TOC entry 4590 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.order_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.order_id IS '智算任务工单id';


--
-- TOC entry 4591 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.plan_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.plan_id IS 'plan_id';


--
-- TOC entry 4592 (class 0 OID 0)
-- Dependencies: 270
-- Name: COLUMN mm_train_task_demo.candidate_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_demo.candidate_id IS 'candidate_id';


--
-- TOC entry 271 (class 1259 OID 306962)
-- Name: mm_train_task_eval; Type: TABLE; Schema: train; Owner: -
--

CREATE TABLE train.mm_train_task_eval (
    id numeric(20,0) NOT NULL,
    train_task_id numeric(20,0) NOT NULL,
    stem numeric(5,2),
    social_science numeric(5,2),
    humanity numeric(5,2),
    other numeric(5,2),
    creator_id numeric(20,0) NOT NULL,
    create_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modifier_id numeric(20,0) NOT NULL,
    modify_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    average numeric(5,2),
    status character varying(16),
    eval_target character varying(16),
    eval_info character varying(255)
);


--
-- TOC entry 4593 (class 0 OID 0)
-- Dependencies: 271
-- Name: TABLE mm_train_task_eval; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON TABLE train.mm_train_task_eval IS '训练任务c-eval 评估';


--
-- TOC entry 4594 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.id IS 'ID';


--
-- TOC entry 4595 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.train_task_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.train_task_id IS '训练任务ID，train.mm_train_task.id';


--
-- TOC entry 4596 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.stem; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.stem IS '理工评分';


--
-- TOC entry 4597 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.social_science; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.social_science IS '社会科学评分';


--
-- TOC entry 4598 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.humanity; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.humanity IS '人文科学评分';


--
-- TOC entry 4599 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.other; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.other IS '其他评分';


--
-- TOC entry 4600 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.creator_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.creator_id IS '创建人ID';


--
-- TOC entry 4601 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.create_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.create_date IS '创建时间';


--
-- TOC entry 4602 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.modifier_id; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.modifier_id IS '修改人';


--
-- TOC entry 4603 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.modify_date; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.modify_date IS '修改时间';


--
-- TOC entry 4604 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.average; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.average IS '平均分';


--
-- TOC entry 4605 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.status; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.status IS '评估状态，字典MODEL_C_EVAL_STATUS';


--
-- TOC entry 4606 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.eval_target; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.eval_target IS '评估接口目标，mate.mm_cluster.code';


--
-- TOC entry 4607 (class 0 OID 0)
-- Dependencies: 271
-- Name: COLUMN mm_train_task_eval.eval_info; Type: COMMENT; Schema: train; Owner: -
--

COMMENT ON COLUMN train.mm_train_task_eval.eval_info IS '评估信息';


--
-- TOC entry 3423 (class 2604 OID 306967)
-- Name: mm_model_monitor_intf id; Type: DEFAULT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_intf ALTER COLUMN id SET DEFAULT nextval('log.mm_model_monitor_intf_id_seq'::regclass);


--
-- TOC entry 3424 (class 2604 OID 306968)
-- Name: mm_model_monitor_model id; Type: DEFAULT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_model ALTER COLUMN id SET DEFAULT nextval('log.mm_model_monitor_model_id_seq'::regclass);


--
-- TOC entry 3425 (class 2604 OID 306969)
-- Name: mm_model_monitor_statistics id; Type: DEFAULT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_statistics ALTER COLUMN id SET DEFAULT nextval('log.mm_model_monitor_statistics_id_seq'::regclass);


--
-- TOC entry 3533 (class 2604 OID 327397)
-- Name: mm_project_space project_id; Type: DEFAULT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_project_space ALTER COLUMN project_id SET DEFAULT nextval('meta.mm_project_space_project_id_seq'::regclass);


--
-- TOC entry 3825 (class 0 OID 306629)
-- Dependencies: 220
-- Data for Name: mm_job; Type: TABLE DATA; Schema: job; Owner: -
--

INSERT INTO job.mm_job VALUES (1797899601941475329, '样例多参数', 'DEMO', 'demoJob.multipleParams("字符串",true,100000000L,0.03D,9)', '0/10 * * * * ?', '0', '0', '1', -1, '2024-06-04 15:54:03.056', -1, '2024-06-04 16:17:17.735', NULL);
INSERT INTO job.mm_job VALUES (1797902258785484801, '样例单参数', 'DEMO', 'demoJob.params("字符串")', '0/10 * * * * ?', '0', '0', '1', -1, '2024-06-04 16:04:35.991', -1, '2024-06-04 19:20:04.386', NULL);
INSERT INTO job.mm_job VALUES (1797921230474022914, '样例无参数', 'DEMO', 'demoJob.noParams()', '0/20 * * * * ?', '0', '0', '1', -1, '2024-06-04 17:19:59.683', -1, '2024-06-04 17:32:34.992', NULL);
INSERT INTO job.mm_job VALUES (1797904538812559333, '[日志中心-模型监控]删除15天数据', 'DEFAULT', 'logModelJob.deleteMoreThanTimeData()', '0 0 0 * * ?', '0', '0', '0', -1, '2024-07-05 11:36:24.243', 0, '2024-07-05 11:36:24.243', '');
INSERT INTO job.mm_job VALUES (1797894538812559366, '清除15天前的模型体验对话日志', 'DEFAULT', 'modelChatLogJob.cleanExpiredData(-15)', '0 0 1 1/1 * ?', '0', '0', '0', -1, '2024-06-04 18:10:00.224', -1, '2024-06-04 18:10:00.224', NULL);
INSERT INTO job.mm_job VALUES (1833369343921274887, '定时获取大模型部署状态_k8s', 'DEFAULT', 'deployJob.callBackDeployStatus()', '0/15 * * * * ?', '0', '0', '0', -1, '2024-06-04 18:10:00.224', -1, '2024-06-04 18:10:00.224', NULL);
INSERT INTO job.mm_job VALUES (1797894538812559362, '训练任务下发大模型', 'DEFAULT', 'trainJob.trainTask()', '0 0/1 * * * ?', '0', '0', '1', -1, '2024-06-04 15:33:55.007', -1, '2024-06-04 15:33:55.008', NULL);
INSERT INTO job.mm_job VALUES (1797894538812559365, '部署任务下发', 'DEFAULT', 'deployJob.deployTask()', '30 * * * * ?', '0', '0', '1', -1, '2024-06-04 15:33:55', -1, '2024-06-24 10:35:26.779', '');
INSERT INTO job.mm_job VALUES (1833580399729942528, '回调获取评估部署任务状态', 'DEFAULT', 'prTestSetEvaluationJob.callbackDeployEvalStatus()', '0 0/1 * * * ?', '0', '0', '0', -1, '2024-09-11 02:58:00.846', -1, '2024-09-11 02:58:00.846', NULL);
INSERT INTO job.mm_job VALUES (1797894538812559367, '删除1天前的部署模型', 'DEFAULT', 'deployJob.deleteExpiredDeployTask(1)', '0 0/10 * * * ?', '0', '0', '0', -1, '2024-06-04 18:10:00.224', -1, '2024-06-04 18:10:00.224', NULL);
INSERT INTO job.mm_job VALUES (1797933815290249218, '清除10天前的任务调度日志', 'DEFAULT', 'cleanLogJob.cleanJobLog(10)', '15 1 0 1/3 * ?', '0', '0', '0', -1, '2024-06-04 18:10:00.224', -1, '2024-06-04 18:10:00.224', NULL);
INSERT INTO job.mm_job VALUES (1797894538812559368, '测试数据集批量推理', 'DEFAULT', 'prTestSetEvaluationJob.batchChatByTestDataSet()', '5 0/1 * * * ? ', '0', '0', '0', -1, '2024-07-04 11:36:24.243', -1, '2024-07-04 11:36:24.243', '');
INSERT INTO job.mm_job VALUES (1877612985595539505, '训练任务获取在k8s排队和在训练的状态信息demo版本', 'DEFAULT', 'trainDemoJob.trainTaskStatus()', '0/10 * * * * ?', '0', '0', '0', 0, '2024-08-19 19:51:26.256', 0, '2024-08-19 19:51:26.256', NULL);
INSERT INTO job.mm_job VALUES (1825395421531729943, '训练任务获取在k8s排队和在训练的状态信息', 'DEFAULT', 'trainJob.trainTaskStatus()', '0 0/2 * * * ? ', '0', '0', '0', 0, '2024-08-19 19:51:26.256', 0, '2024-08-19 19:51:26.256', NULL);
INSERT INTO job.mm_job VALUES (1877612985595539516, '定时训练任务发送inference', 'DEFAULT', 'trainJob.trainTaskSend()', '0 0/1 * * * ? ', '0', '0', '0', 0, '2024-08-19 19:51:26.256', 0, '2024-08-19 19:51:26.256', NULL);
INSERT INTO job.mm_job VALUES (1877612985595539517, '定时部署任务发送inference', 'DEFAULT', 'deployJob.callBackDeploySend()', '0 0/1 * * * ? ', '0', '0', '0', 0, '2024-08-19 19:51:26.256', 0, '2024-08-19 19:51:26.256', NULL);
INSERT INTO job.mm_job VALUES (1877612985595539518, '定时评测任务发送inference', 'DEFAULT', 'prTestSetEvaluationJob.submitEvaluationDeploymentTask()', '0 0/1 * * * ? ', '0', '0', '0', 0, '2024-08-19 19:51:26.256', 0, '2024-08-19 19:51:26.256', NULL);


--
-- TOC entry 3826 (class 0 OID 306636)
-- Dependencies: 221
-- Data for Name: mm_job_log; Type: TABLE DATA; Schema: job; Owner: -
--



--
-- TOC entry 3827 (class 0 OID 306641)
-- Dependencies: 222
-- Data for Name: conversation_history; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3828 (class 0 OID 306646)
-- Dependencies: 223
-- Data for Name: mm_log; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3829 (class 0 OID 306655)
-- Dependencies: 224
-- Data for Name: mm_menu_click_log; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3830 (class 0 OID 306658)
-- Dependencies: 225
-- Data for Name: mm_model_chat_log; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3831 (class 0 OID 306671)
-- Dependencies: 226
-- Data for Name: mm_model_monitor_intf; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3833 (class 0 OID 306675)
-- Dependencies: 228
-- Data for Name: mm_model_monitor_model; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3835 (class 0 OID 306679)
-- Dependencies: 230
-- Data for Name: mm_model_monitor_statistics; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3837 (class 0 OID 306683)
-- Dependencies: 232
-- Data for Name: mm_user_login_log; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3878 (class 0 OID 327380)
-- Dependencies: 273
-- Data for Name: session_cache; Type: TABLE DATA; Schema: log; Owner: -
--



--
-- TOC entry 3838 (class 0 OID 306686)
-- Dependencies: 233
-- Data for Name: mm_application_square; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_application_square VALUES (1808053568557809671, '传输故障处置助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '基于故障工单生成故障原因和故障解决方案，辅助运维人员快速定位、处置故障', 0, '2024-07-02 16:52:13.401', 0, '2024-07-02 16:52:13.401', '1', '123456', '1', '1');
INSERT INTO meta.mm_application_square VALUES (1808053568557809672, '综维知识助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '提供综维知识和专家经验，辅助一线人员进行维护操作，提升维护效率，保证处置的规范性。', 0, '2024-07-02 16:52:13.401', 0, '2024-07-02 16:52:13.401', '2', '123456', '0', '1');
INSERT INTO meta.mm_application_square VALUES (1808053568557809673, '无线网优知识助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '快速响应无线网优名类咨询，支持查询无线网优知识、方案等，共享复用优秀知识经验', 0, '2024-07-02 16:52:13.401', 0, '2024-07-02 16:52:13.401', '3', '123456', '0', '1');
INSERT INTO meta.mm_application_square VALUES (1808053568557809674, '故障复盘助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '基于已发生故障信息记性复盘生成故障复盘报告。', 0, '2024-07-02 16:52:13.401', 0, '2024-07-02 16:52:13.401', '6', '123456', '1', '1');
INSERT INTO meta.mm_application_square VALUES (1808053568557809675, '装维知识助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '支持快速查询装维相关知识，包括基础知识、配置指导、流程政策等。', 0, '2024-07-02 16:52:13.401', 0, '2024-07-02 16:52:13.401', '7', '123456', '0', '1');
INSERT INTO meta.mm_application_square VALUES (1800449129470828544, '规章制度知识助手', '2', 'ws://192.168.0.1:9101/ws/qiming_scene', '提供规章制度、安全知识问答能力', 0, '2024-06-11 16:44:57.85', 0, '2024-06-19 11:30:53.808', '8', '123456', '0', '1');
INSERT INTO meta.mm_application_square VALUES (1800448980430430208, '家宽装维智能体', '-1', 'ws://192.168.0.1:9101/ws/qiming_scene', '基于智能体能力提供客户信息查询、工单状态查询、网络状态诊断等能力', 0, '2024-06-11 16:44:22.32', 0, '2024-06-19 11:30:53.753', '7', '123456', NULL, '1');


--
-- TOC entry 3839 (class 0 OID 306695)
-- Dependencies: 234
-- Data for Name: mm_cluster; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_cluster VALUES (1838400497321893888, '青岛', 'QD', '山东', 'http://192.168.0.1:20000', 'http://192.168.0.1:20038', 'http://192.168.0.1:20038', 'http://192.168.0.1:20018', 0, '2024-09-24 10:19:42.418', 0, '2024-09-24 10:19:42.418', 'http://192.168.0.1:20038');
INSERT INTO meta.mm_cluster VALUES (1838400497321893889, '贵州', 'GZ', '贵州', 'http://192.168.0.2:20000', 'http://192.168.0.2:20038', 'http://192.168.0.2:20038', 'http://192.168.0.2:20018', 0, '2024-09-24 10:19:42.502', 0, '2024-09-24 10:19:42.502', 'http://192.168.0.2:20038');


--
-- TOC entry 3840 (class 0 OID 306702)
-- Dependencies: 235
-- Data for Name: mm_cluster_metric; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303362, '集群GPU显存总量', 'nvidia-gpu-total', 'gpuTotal', 'QD', 'RESOURCE_USAGE', 'sum(nvidia_gpu_memory_total_bytes)', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303363, '集群GPU显存剩余量', 'nvidia-gpu-free', 'gpuFree', 'QD', 'RESOURCE_USAGE', 'sum(nvidia_gpu_memory_total_bytes)-sum(nvidia_gpu_memory_used_bytes)', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303374, '集群可用算力卡（节点维度）', 'nvidia-gpu-card-avail', 'gpuCardAvail', 'QD', 'CLUSTER_DETAIL', 'count by (instance) (nvidia_gpu_memory_used_bytes/1024/1024/1024<2)', '2024-09-25 19:02:32.02', '2024-09-25 19:02:32.02', NULL, NULL, '0');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303360, '集群CPU总量', 'cpu-total', 'cpuTotal', 'QD', 'RESOURCE_USAGE', 'sum(instance:node_num_cpu:sum)', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, '', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303364, '集群POD总量', 'pod-count', 'podCount', 'QD', 'RESOURCE_COUNT', 'count(kube_pod_info{namespace=~"qwen2-train|qwen2-inference"})', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303365, '集群POD总量', 'pod-count', 'podCount', 'GZ', 'RESOURCE_COUNT', 'count(kube_pod_info{namespace=~"qwen2-train|qwen2-inference"})', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220993, '集群内存剩余量', 'memory-free', 'memoryFree', 'QD', 'RESOURCE_USAGE', 'sum(node_memory_MemFree_bytes)', '2024-09-26 17:33:04.576', '2024-09-26 17:33:04.576', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220994, '集群存储总量', 'storage-total', 'storageTotal', 'QD', 'RESOURCE_USAGE', 'sum(node_filesystem_size_bytes)', '2024-09-26 17:39:46.337', '2024-09-26 17:39:46.337', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838929925709758467, '集群内存使用率', 'memory-usage', 'memoryUsage', 'GZ', 'CLUSTER_DETAIL', 'round((1-sum(node_memory_MemFree_bytes)/sum(node_memory_MemTotal_bytes))*100,0.01)', '2024-09-25 21:23:15.692', '2024-09-25 21:23:15.692', NULL, '%', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838929925709758464, '集群内存使用率', 'memory-usage', 'memoryUsage', 'QD', 'CLUSTER_DETAIL', 'round((1-sum(node_memory_MemFree_bytes)/sum(node_memory_MemTotal_bytes))*100,0.01)', '2024-09-25 21:19:44.66', '2024-09-25 21:19:44.66', NULL, '%', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838929925709758466, '集群CPU使用率', 'cpu-usage', 'cpuUsage', 'GZ', 'CLUSTER_DETAIL', 'round(avg(1-sum by(instance)(increase(node_cpu_seconds_total{mode="idle"}[2m]))/sum by(instance)(increase(node_cpu_seconds_total[2m])))*100,0.01)', '2024-09-25 21:23:15.61', '2024-09-25 21:23:15.61', NULL, '%', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838929925705564160, '集群CPU使用率', 'cpu-usage', 'cpuUsage', 'QD', 'CLUSTER_DETAIL', 'round(avg(1-sum by(instance)(increase(node_cpu_seconds_total{mode="idle"}[2m]))/sum by(instance)(increase(node_cpu_seconds_total[2m])))*100,0.01)', '2024-09-25 21:19:44.548', '2024-09-25 21:19:44.548', NULL, '%', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860967026688, '集群CPU剩余量', 'cpu-free', 'cpuFree', 'QD', 'RESOURCE_USAGE', 'round(sum(instance:node_num_cpu:sum)*avg(sum by(instance)(increase(node_cpu_seconds_total{mode="idle"}[2m]))/sum by(instance)(increase(node_cpu_seconds_total[2m]))),1)', '2024-09-26 17:16:12.686', '2024-09-26 17:16:12.686', NULL, '', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220992, '集群内存总量', 'memory-total', 'memoryTotal', 'QD', 'RESOURCE_USAGE', 'sum(node_memory_MemTotal_bytes)', '2024-09-26 17:20:01.277', '2024-09-26 17:20:01.277', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220995, '集群存储剩余量', 'storage-free', 'storageFree', 'QD', 'RESOURCE_USAGE', 'sum(node_filesystem_avail_bytes)', '2024-09-26 17:43:05.617', '2024-09-26 17:43:05.617', '', 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303366, '集群可用算力卡', 'nvidia-gpu-card-avail-total', 'availableCardCount', 'QD', 'RESOURCE_COUNT', 'sum(count(nvidia_gpu_memory_used_bytes/1024/1024/1024<2))', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838929925709758465, '集群可用算力卡（节点维度）', 'ascend-gpu-card-avail', 'gpuCardAvail', 'GZ', 'CLUSTER_DETAIL', 'count by (instance) (npu_chip_info_hbm_used_memory/1024<5)', '2024-09-25 21:23:15.521', '2024-09-25 21:23:15.521', NULL, NULL, '0');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303376, '集群可用算力卡', 'ascend-npu-card-avail-total', 'availableCardCount', 'GZ', 'RESOURCE_COUNT', 'sum(count(npu_chip_info_hbm_used_memory/1024<5))', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220996, '集群服务器可用', 'nvidia-server-avail', 'serverFree', 'QD', 'RESOURCE_USAGE', 'count(count by (instance) (nvidia_gpu_memory_used_bytes/1024/1024/1024<2))', '2024-09-26 17:49:32.561', '2024-09-26 17:49:32.561', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221000, '集群CPU总量', 'cpu-total', 'cpuTotal', 'GZ', 'RESOURCE_USAGE', 'sum(instance:node_num_cpu:sum)', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, '', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221003, '集群内存剩余量', 'memory-free', 'memoryFree', 'GZ', 'RESOURCE_USAGE', 'sum(node_memory_MemFree_bytes)', '2024-09-26 17:33:04.576', '2024-09-26 17:33:04.576', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221004, '集群内存总量', 'memory-total', 'memoryTotal', 'GZ', 'RESOURCE_USAGE', 'sum(node_memory_MemTotal_bytes)', '2024-09-26 17:20:01.277', '2024-09-26 17:20:01.277', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860987998208, '集群存储剩余量', 'storage-free', 'storageFree', 'GZ', 'RESOURCE_USAGE', 'sum(node_filesystem_avail_bytes)', '2024-09-26 17:43:05.617', '2024-09-26 17:43:05.617', '', 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192512, '集群存储总量', 'storage-total', 'storageTotal', 'GZ', 'RESOURCE_USAGE', 'sum(node_filesystem_size_bytes)', '2024-09-26 17:39:46.337', '2024-09-26 17:39:46.337', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192526, '集群资源使用趋势-内存使用量', 'cluster-usage-trend-memory-used', 'memoryUsed', 'GZ', 'CLUSTER_USAGE_TREND', 'sum(node_memory_MemTotal_bytes-node_memory_MemFree_bytes) by (time)', '2024-09-25 18:58:31.861', '2024-09-25 18:58:31.861', '内存使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192527, '集群资源使用趋势-存储使用量', 'cluster-usage-trend-storage-used', 'storageUsed', 'QD', 'CLUSTER_USAGE_TREND', 'sum(node_filesystem_size_bytes-node_filesystem_avail_bytes) by (time)', '2024-09-25 18:45:54.642', '2024-09-25 18:45:54.642', '存储使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220999, '集群CPU剩余量', 'cpu-free', 'cpuFree', 'GZ', 'RESOURCE_USAGE', 'round(sum(instance:node_num_cpu:sum)*avg(sum by(instance)(increase(node_cpu_seconds_total{mode="idle"}[2m]))/sum by(instance)(increase(node_cpu_seconds_total[2m]))),1)', '2024-09-26 17:16:12.686', '2024-09-26 17:16:12.686', NULL, '', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192521, '集群资源使用趋势-CPU使用量', 'cluster-usage-trend-cpu-used', 'cpuUsed', 'QD', 'CLUSTER_USAGE_TREND', 'round(sum(node_load5) by (time) * 100) / 100', '2024-09-25 18:45:54.642', '2024-09-25 18:45:54.642', 'CPU使用量', 'GHz', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192522, '集群资源使用趋势-CPU使用量', 'cluster-usage-trend-cpu-used', 'cpuUsed', 'GZ', 'CLUSTER_USAGE_TREND', 'round(sum(node_load5) by (time) * 100) / 100', '2024-09-25 18:58:31.861', '2024-09-25 18:58:31.861', 'CPU使用量', 'GHz', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192523, '集群资源使用趋势-GPU使用量', 'cluster-usage-trend-gpu-used', 'gpuUsed', 'QD', 'CLUSTER_USAGE_TREND', 'sum(nvidia_gpu_memory_used_bytes) by (time)', '2024-09-25 18:45:54.642', '2024-09-25 18:45:54.642', 'GPU使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192525, '集群资源使用趋势-内存使用量', 'cluster-usage-trend-memory-used', 'memoryUsed', 'QD', 'CLUSTER_USAGE_TREND', 'sum(node_memory_MemTotal_bytes-node_memory_MemFree_bytes) by (time)', '2024-09-25 18:45:54.642', '2024-09-25 18:45:54.642', '内存使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192528, '集群资源使用趋势-存储使用量', 'cluster-usage-trend-storage-used', 'storageUsed', 'GZ', 'CLUSTER_USAGE_TREND', 'sum(node_filesystem_size_bytes-node_filesystem_avail_bytes) by (time)', '2024-09-25 18:58:31.861', '2024-09-25 18:58:31.861', '存储使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221002, '集群GPU显存总量', 'ascend-gpu-total', 'gpuTotal', 'GZ', 'RESOURCE_USAGE', 'sum(npu_chip_info_hbm_total_memory)*1024*1024', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220997, '集群服务器总量', 'nvidia-server-total', 'serverTotal', 'QD', 'RESOURCE_USAGE', 'count(node:node_num_cpu:sum)', '2024-09-26 17:52:29.708', '2024-09-26 17:52:29.708', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860992192524, '集群资源使用趋势-GPU使用量', 'cluster-usage-trend-gpu-used', 'gpuUsed', 'GZ', 'CLUSTER_USAGE_TREND', 'sum(npu_chip_info_hbm_used_memory) by (time) * 1024 * 1024', '2024-09-25 18:58:31.861', '2024-09-25 18:58:31.861', 'GPU使用量', 'GB', '0');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221005, '集群服务器可用', 'ascend-server-avail', 'serverFree', 'GZ', 'RESOURCE_USAGE', 'count(count by (instance) (npu_chip_info_hbm_used_memory/1024<5))', '2024-09-26 17:49:32.561', '2024-09-26 17:49:32.561', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303369, 'GPU显卡温度', 'nvidia-gpu-temperature', 'gpuTemperature', 'QD', 'RESOURCE_USAGE_DETAIL', 'nvidia_gpu_temperature_celsius', '2024-09-26 09:46:52.732', '2024-09-26 09:46:52.732', NULL, '℃', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303373, 'GPU显卡温度', 'npu-chip-info-temperature', 'gpuTemperature', 'GZ', 'RESOURCE_USAGE_DETAIL', 'npu_chip_info_temperature', '2024-09-26 10:17:32.999', '2024-09-26 10:17:32.999', NULL, '℃', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303372, 'GPU显存使用量', 'npu-chip-info-hbm-used-memory', 'gpuMemoryUsed', 'GZ', 'RESOURCE_USAGE_DETAIL', 'npu_chip_info_hbm_used_memory', '2024-09-26 10:17:32.954', '2024-09-26 10:17:32.954', NULL, 'M', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303371, 'GPU显存总量', 'npu_chip-info-hbm-total-memory', 'gpuMemoryTotal', 'GZ', 'RESOURCE_USAGE_DETAIL', 'npu_chip_info_hbm_total_memory', '2024-09-26 10:16:11.38', '2024-09-26 10:16:11.38', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303368, 'GPU显存总量', 'nvidia-gpu-memory-total', 'gpuMemoryTotal', 'QD', 'RESOURCE_USAGE_DETAIL', 'nvidia_gpu_memory_total_bytes', '2024-09-26 09:45:32.882', '2024-09-26 09:45:32.882', NULL, 'M', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303370, 'GPU功率', 'nvidia-gpu-power', 'gpuPowerUsage', 'QD', 'RESOURCE_USAGE_DETAIL', 'nvidia_gpu_power_usage_milliwatts', '2024-09-26 09:47:56.398', '2024-09-26 09:47:56.398', NULL, 'mW', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303375, 'GPU功率', 'npu-chip-info-power', 'gpuPowerUsage', 'GZ', 'RESOURCE_USAGE_DETAIL', 'npu_chip_info_power', '2024-09-26 10:18:28.636', '2024-09-26 10:18:28.636', NULL, 'W', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1838864011484303367, 'GPU显存使用量', 'nvidia-gpu-memory-used', 'gpuMemoryUsed', 'QD', 'RESOURCE_USAGE_DETAIL', 'nvidia_gpu_memory_used_bytes', '2024-09-26 09:43:56.044', '2024-09-26 09:43:56.044', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221001, '集群GPU显存剩余量', 'ascend-gpu-free', 'gpuFree', 'GZ', 'RESOURCE_USAGE', '(sum(npu_chip_info_hbm_total_memory)-sum(npu_chip_info_hbm_used_memory))*1024*1024', '2024-09-25 17:20:03.651', '2024-09-25 17:20:03.651', NULL, 'B', '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971220927, '集群服务器总量', 'nvidia-server-total', 'serverCount', 'QD', 'RESOURCE_COUNT', 'count(node:node_num_cpu:sum)', '2024-09-26 17:52:29.708', '2024-09-26 17:52:29.708', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221006, '集群服务器总量', 'ascend-server-total', 'serverTotal', 'GZ', 'RESOURCE_USAGE', 'count(machine_cpu_cores)', '2024-09-26 17:52:29.708', '2024-09-26 17:52:29.708', NULL, NULL, '1');
INSERT INTO meta.mm_cluster_metric VALUES (1839231860971221026, '集群服务器总量', 'ascend-server-total', 'serverCount', 'GZ', 'RESOURCE_COUNT', 'count(machine_cpu_cores)', '2024-09-26 17:52:29.708', '2024-09-26 17:52:29.708', NULL, NULL, '1');


--
-- TOC entry 3841 (class 0 OID 306710)
-- Dependencies: 236
-- Data for Name: mm_data_set; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3842 (class 0 OID 306722)
-- Dependencies: 237
-- Data for Name: mm_data_set_file; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3843 (class 0 OID 306731)
-- Dependencies: 238
-- Data for Name: mm_dict_data; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_dict_data VALUES (1783028812460965891, 1, '系统模版', '1', 'PROMPT_BELONG', NULL, NULL, NULL, '0', NULL, '2024-04-23 13:48:45.899577', NULL, '2024-04-23 13:48:45.899577', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965892, 2, '自定义模版', '2', 'PROMPT_BELONG', NULL, NULL, NULL, '0', NULL, '2024-04-23 13:48:45.944629', NULL, '2024-04-23 13:48:45.944629', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965896, 1, '规章制度知识助手', '1', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965897, 2, '综维知识助手', '2', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965898, 3, '无线网优知识助手', '3', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965899, 4, '装维知识助手', '4', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965900, 5, '故障复盘助手', '5', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965901, 6, '传输故障处置助手', '6', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965902, 7, '核心网高危指令稽核助手', '7', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965903, 8, '家宽装维自服务智能体', '8', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965904, 9, 'PaaS知识助手', '9', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965905, 10, '业务工单卡单自愈处置助手', '10', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965906, 11, 'IP配置稽核助手', '11', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965907, 12, '无线故障处置助手', '12', 'PROMPT_TYPE', NULL, NULL, NULL, '0', NULL, '2024-04-24 09:35:48.268', NULL, '2024-04-24 09:35:48.268', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965910, 1, '是', '10050001', '1005', NULL, NULL, NULL, '0', NULL, '2024-04-24 17:28:54.746656', NULL, '2024-04-24 17:28:54.746656', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965911, 2, '否', '10050002', '1005', NULL, NULL, NULL, '0', NULL, '2024-04-24 17:29:18.597615', NULL, '2024-04-24 17:29:18.597615', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637635, 1, '排队中', 'waiting', 'TRAIN_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-05-15 12:45:31.316', NULL, '2024-05-15 12:45:31.316', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637636, 2, '训练中', 'training', 'TRAIN_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-05-15 12:45:31.399', NULL, '2024-05-15 12:45:31.399', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637637, 3, '训练完成', 'completed', 'TRAIN_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-05-15 12:45:31.521', NULL, '2024-05-15 12:45:31.521', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965893, 1, '小括号()', '1', 'PROMPT_IDENTIFIER', NULL, NULL, NULL, '1', NULL, '2024-04-23 13:50:48.173334', NULL, '2024-05-28 07:18:08.049734', NULL, NULL, '\$\(([^()]+)\)', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965894, 2, '中括号[]', '2', 'PROMPT_IDENTIFIER', NULL, NULL, NULL, '1', NULL, '2024-04-23 13:50:48.224107', NULL, '2024-05-28 07:18:08.051016', NULL, NULL, '\$\[([^]]+)\]', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965909, 1, '服务器2', '1', 'PROMPT_OPTIMIZE_MODEL', NULL, NULL, NULL, '0', NULL, '2024-04-24 17:29:18.597615', NULL, '2024-05-28 07:18:08.05156', NULL, NULL, 'http://10.127.31.13:9104/v2/start_chat', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573953, 1, '大模型', '1', 'APPLICATION_TYPE', NULL, NULL, NULL, '0', NULL, '2024-06-11 09:06:49.727', NULL, '2024-06-11 09:06:49.727', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573954, 2, '知识助手', '2', 'APPLICATION_TYPE', NULL, NULL, NULL, '0', NULL, '2024-06-11 09:06:49.813', NULL, '2024-06-11 09:06:49.813', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573956, 1, '传输故障处置助手', '1', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.74', NULL, '2024-06-11 16:36:21.74', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573957, 2, '综维知识助手', '2', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.792', NULL, '2024-06-11 16:36:21.792', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573958, 3, '无线网优知识助手', '3', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.848', NULL, '2024-06-11 16:36:21.848', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573959, 4, '故障复盘助手', '6', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.896', NULL, '2024-06-11 16:36:21.896', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573960, 5, '装维知识助手', '7', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.94', NULL, '2024-06-11 16:36:21.94', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1800332127544573961, 6, '规章制度知识助手', '8', 'APPLICATION_SCENE', NULL, NULL, NULL, '0', NULL, '2024-06-11 16:36:21.985', NULL, '2024-06-11 16:36:21.985', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637638, 4, '训练失败', 'failed', 'TRAIN_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-05-15 12:45:31.563', NULL, '2024-06-25 16:33:21.860887', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1806606876608319490, 1, '系统模型', '1', 'MODEL_BELONG', NULL, NULL, NULL, '0', NULL, '2024-06-28 17:28:13.561', NULL, '2024-06-28 17:28:13.561', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1806606876608319491, 2, '自定义模型', '2', 'MODEL_BELONG', NULL, NULL, NULL, '0', NULL, '2024-06-28 17:28:13.643', NULL, '2024-06-28 17:28:13.643', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1807951382385549313, 1, '问答', 'QandA', 'MODEL_EXPERIENCE_SHAPE', NULL, NULL, NULL, '0', NULL, '2024-07-02 09:39:53.07', NULL, '2024-07-02 09:39:53.07', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1807951382385549314, 2, '图形', 'graph', 'MODEL_EXPERIENCE_SHAPE', NULL, NULL, NULL, '0', NULL, '2024-07-02 09:39:53.199', NULL, '2024-07-02 09:39:53.199', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637838, 1, '部署中', 'deploying', 'DEPLOY_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:47.511', NULL, '2024-07-10 10:54:47.511', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637839, 2, '部署完成', 'completed', 'DEPLOY_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:47.868', NULL, '2024-07-10 10:54:47.868', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637840, 3, '部署失败', 'failed', 'DEPLOY_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.179', NULL, '2024-07-10 10:54:48.179', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637841, 4, '等待中', 'waiting', 'DEPLOY_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1813040121084506113, 1, '系统应用', '1', 'APPLICATION_BELONG', NULL, NULL, NULL, '0', NULL, '2024-07-16 10:41:26.218', NULL, '2024-07-16 10:41:26.218', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1813040121084506114, 2, '自定义应用', '2', 'APPLICATION_BELONG', NULL, NULL, NULL, '0', NULL, '2024-07-16 10:41:26.298', NULL, '2024-07-16 10:41:26.298', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1813417537736867845, 3, '自建模型', '3', 'APPLICATION_TYPE', NULL, NULL, NULL, '0', NULL, '2024-07-17 11:37:49.378', NULL, '2024-07-17 11:37:49.378', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1820390389576957955, 1, '是', '0', 'YSE_OR_NO', NULL, NULL, NULL, '0', NULL, '2024-08-05 17:32:37.022561', NULL, '2024-08-05 17:32:37.022561', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1820390389576957956, 2, '否', '1', 'YSE_OR_NO', NULL, NULL, NULL, '0', NULL, '2024-08-05 17:32:37.102385', NULL, '2024-08-05 17:32:37.102385', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1820390389576957957, 1, '管理员', '1', 'USER_ROLE', NULL, NULL, NULL, '0', NULL, '2024-08-05 17:31:48.763', NULL, '2024-08-05 17:31:48.763', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1820390389576957958, 2, '区域管理员', '2', 'USER_ROLE', NULL, NULL, NULL, '0', NULL, '2024-08-05 17:31:48.851', NULL, '2024-08-05 17:31:48.851', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1820390389576957959, 3, '普通用户', '3', 'USER_ROLE', NULL, NULL, NULL, '0', NULL, '2024-08-05 17:31:48.951', NULL, '2024-08-05 17:31:48.951', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637842, 5, '系统移除', 'removed', 'DEPLOY_TASK_STATUS', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1823985532368408576, 2, 'Lora', 'LORA', 'MODEL_TRAIN_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-15 15:32:43.821', NULL, '2024-08-15 15:32:43.821', NULL, NULL, '2', 'method', NULL);
INSERT INTO meta.mm_dict_data VALUES (1808053568557809665, 1, '全参', 'SFT', 'MODEL_TRAIN_TYPE', NULL, NULL, NULL, '0', NULL, '2024-07-02 16:21:22.229', NULL, '2024-08-21 18:15:14.770438', NULL, NULL, '1', 'method', NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637634, 1, '全参', '1', 'TRAIN_TASK_METHOD', NULL, NULL, NULL, '0', NULL, '2024-05-15 12:45:31.221', NULL, '2024-08-21 18:15:15.105535', NULL, NULL, 'full', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1806606876608319492, 2, 'Lora', '2', 'TRAIN_TASK_METHOD', NULL, NULL, NULL, '0', NULL, '2024-06-28 17:28:13.703', NULL, '2024-08-21 18:15:15.178669', NULL, NULL, 'lora', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1823985532368408578, 3, 'Q-Lora', '3', 'TRAIN_TASK_METHOD', NULL, NULL, NULL, '0', NULL, '2024-08-15 15:39:51.677', NULL, '2024-08-15 15:39:51.677', NULL, NULL, 'qlora', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140392, 1, 'Prompt+Response', '1', 'DATA_SET_DATA_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140395, 1, '训练集', '1', 'DATA_SET_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140394, 3, '意图识别', '3', 'DATA_SET_DATA_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140393, 2, '流量预测', '2', 'DATA_SET_DATA_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140396, 2, '测试集', '2', 'DATA_SET_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1823985532368408577, 3, 'Q-Lora', 'QLORA', 'MODEL_TRAIN_TYPE', NULL, NULL, NULL, '1', NULL, '2024-08-15 15:32:44.138', NULL, '2024-11-22 17:00:54.225724', NULL, NULL, '3', 'method', NULL);
INSERT INTO meta.mm_dict_data VALUES (1783028812460965895, 3, '大括号{}', '3', 'PROMPT_IDENTIFIER', NULL, NULL, NULL, '0', NULL, '2024-04-23 13:50:48.268295', NULL, '2024-05-28 07:18:08.051295', NULL, NULL, '\{\{([^}]+)\}\}', NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1808053568557809666, 4, '意图识别训练', 'IR', 'MODEL_TRAIN_TYPE', NULL, NULL, NULL, '1', NULL, '2024-07-02 16:21:22.229', NULL, '2024-08-21 18:15:14.697434', NULL, NULL, '2', 'target', NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156608, 1, '评估中', 'evaluating', 'MODEL_C_EVAL_STATUS', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.621', NULL, '2024-09-05 14:24:23.621', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156610, 3, '评估失败', 'failed', 'MODEL_C_EVAL_STATUS', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.775', NULL, '2024-09-05 14:24:23.775', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156611, 4, '评估出错', 'error', 'MODEL_C_EVAL_STATUS', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.868', NULL, '2024-09-05 14:24:23.868', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637851, 1, '排队中', 'waiting', 'DATA_SET_EVALUATION', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637852, 2, '评估中', 'evaluating', 'DATA_SET_EVALUATION', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637853, 3, '评估完成', 'completed', 'DATA_SET_EVALUATION', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1790602204269637854, 4, '评估失败', 'failed', 'DATA_SET_EVALUATION', NULL, NULL, NULL, '0', NULL, '2024-07-10 10:54:48.306', NULL, '2024-07-10 10:54:48.306', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156621, 1, '训练', '1', 'DEPLOY_TYPE', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.868', NULL, '2024-09-05 14:24:23.868', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156622, 2, '测试', '2', 'DEPLOY_TYPE', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.868', NULL, '2024-09-05 14:24:23.868', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1832984485747335168, 1, '青岛', 'QD', 'TRAIN_DEPLOY_TARGET', NULL, NULL, NULL, '0', NULL, '2024-09-09 11:32:00.014', NULL, '2024-09-09 11:32:00.014', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1832984485747335169, 2, '贵州', 'GZ', 'TRAIN_DEPLOY_TARGET', NULL, NULL, NULL, '0', NULL, '2024-09-09 11:32:00.333', NULL, '2024-09-09 11:32:00.333', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1831575530345156609, 2, '评估成功', 'completed', 'MODEL_C_EVAL_STATUS', NULL, NULL, NULL, '0', NULL, '2024-09-05 14:24:23.7', NULL, '2024-09-05 14:24:23.7', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140289, 0, '1小时', '1', 'PREDICTION_DURATION', NULL, NULL, NULL, '0', NULL, '2024-09-12 07:30:11.776772', NULL, '2024-09-12 07:30:11.776772', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140292, 1, '3小时', '3', 'PREDICTION_DURATION', NULL, NULL, NULL, '0', NULL, '2024-09-12 07:30:11.77827', NULL, '2024-09-12 07:30:11.77827', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140290, 2, '6小时', '6', 'PREDICTION_DURATION', NULL, NULL, NULL, '0', NULL, '2024-09-12 07:30:11.778627', NULL, '2024-09-12 07:30:11.778627', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140291, 3, '21小时', '21', 'PREDICTION_DURATION', NULL, NULL, NULL, '0', NULL, '2024-09-12 07:30:11.778939', NULL, '2024-09-12 07:30:11.778939', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315963822082, 5, 'Lora', 'RL-LORA', 'MODEL_TRAIN_TYPE', NULL, NULL, NULL, '0', NULL, '2024-11-04 17:41:27.383576', NULL, '2024-11-04 17:41:27.383576', NULL, NULL, '2', 'learn', NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315968016384, 1, 'SFT训练', 'method', 'MODEL_TRAIN_CLASSIFY', NULL, NULL, NULL, '0', NULL, '2024-11-04 17:41:37.790274', NULL, '2024-11-04 17:41:37.790274', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315968016385, 2, '目标训练', 'target', 'MODEL_TRAIN_CLASSIFY', NULL, NULL, NULL, '0', NULL, '2024-11-04 17:41:37.848933', NULL, '2024-11-04 17:41:37.848933', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315968016386, 3, '强化学习', 'learn', 'MODEL_TRAIN_CLASSIFY', NULL, NULL, NULL, '0', NULL, '2024-11-04 17:41:37.901644', NULL, '2024-11-04 17:41:37.901644', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1824280846832140397, 3, '强化学习', '3', 'DATA_SET_TYPE', NULL, NULL, NULL, '0', NULL, '2024-08-16 11:15:27.415', NULL, '2024-08-16 11:15:27.415', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315968016387, 1, '强化学习', '0', 'EVALUATION_TYPE', NULL, NULL, NULL, '0', NULL, '2024-10-24 15:42:41.136', NULL, '2024-10-24 15:42:41.136', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1849008315968016388, 2, '普通', '1', 'EVALUATION_TYPE', NULL, NULL, NULL, '0', NULL, '2024-10-24 15:42:41.184', NULL, '2024-10-24 15:42:41.184', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343100952579, 2, '时序大模型', '2', 'MODEL_TYPE', NULL, NULL, NULL, '0', NULL, '2024-11-19 11:42:03.059', NULL, '2024-11-19 11:42:03.059', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343100952580, 3, '多模态模型', '3', 'MODEL_TYPE', NULL, NULL, NULL, '0', NULL, '2024-11-19 11:42:03.139', NULL, '2024-11-19 11:42:03.139', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343100952581, 1, '模型训练', '1', 'MODEL_AUTH_USAGE', NULL, NULL, NULL, '0', NULL, '2024-11-19 11:42:03.219', NULL, '2024-11-19 11:42:03.219', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146880, 2, '模型推理', '2', 'MODEL_AUTH_USAGE', NULL, NULL, NULL, '0', NULL, '2024-11-19 11:42:03.299', NULL, '2024-11-19 11:42:03.299', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343100952578, 1, '大语言模型', '1', 'MODEL_TYPE', NULL, NULL, NULL, '0', NULL, '2024-11-19 11:42:02.954', NULL, '2024-11-19 11:42:02.954', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146881, 1, '审核中', '1', 'ORDER_REVIEW_STATUS', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146882, 2, '审核结束', '2', 'ORDER_REVIEW_STATUS', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146991, 1, '是否为翻拍', '10001', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146892, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146992, 2, '是否包含条码', '10002', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146892, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146993, 3, '条码是否粘贴在设备上', '10003', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146892, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146994, 4, '输出端口、二维码坐标', '20001', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146893, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146995, 5, '门禁开关', '30001', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146996, 6, '布线不规整', '30002', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146997, 7, '杂物', '30003', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146998, 8, '渗透水', '30004', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105147000, 10, '烟雾', '30006', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146892, 1, '合规性检测', '10', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146894, 3, '机房巡检', '30', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146893, 2, '端口、二维码目标检测', '20', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105146999, 9, '火焰', '30005', 'MULTI_MODAL_TASK', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, 1858715343105146894, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148000, 1, '是否为翻拍', '10001', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148001, 2, '是否包含条码', '10002', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148002, 3, '条码是否粘贴在设备上', '10003', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148003, 4, '输出端口、二维码坐标', '20001', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148004, 5, '门禁开关', '30001', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148005, 6, '布线不规整', '30002', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148006, 7, '杂物', '30003', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148007, 8, '渗透水', '30004', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148008, 9, '火焰', '30005', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_dict_data VALUES (1858715343105148009, 10, '烟雾', '30006', 'MULTI_MODAL_TASK_MAP', NULL, NULL, NULL, '0', NULL, '2024-12-06 15:49:48.863', NULL, '2024-12-06 15:49:48.863', NULL, NULL, NULL, NULL, NULL);


--
-- TOC entry 3844 (class 0 OID 306737)
-- Dependencies: 239
-- Data for Name: mm_dict_type; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_dict_type VALUES (1783028812460965888, 'promp类别', 'PROMPT_TYPE', '0', NULL, '2024-04-23 13:47:05.480734', NULL, '2024-04-23 13:47:05.480734', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1783028812460965889, 'prompt归属', 'PROMPT_BELONG', '0', NULL, '2024-04-23 13:47:05.618997', NULL, '2024-04-23 13:47:05.618997', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1783028812460965890, 'prompt变量标识符', 'PROMPT_IDENTIFIER', '0', NULL, '2024-04-23 13:47:05.756313', NULL, '2024-04-23 13:47:05.756313', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1783028812460965912, 'prompt优化服务选择', 'PROMPT_OPTIMIZE_MODEL', '0', NULL, '2024-04-24 17:22:59.417711', NULL, '2024-04-24 17:22:59.417711', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1783028812460965891, '是/否', '1005', '0', NULL, '2024-04-24 17:22:59.417711', NULL, '2024-04-24 17:22:59.417711', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1790602204269637632, '训练任务方法', 'TRAIN_TASK_METHOD', '0', NULL, '2024-05-15 12:39:16.381', NULL, '2024-05-15 12:39:16.381', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1790602204269637633, '训练任务状态', 'TRAIN_TASK_STATUS', '0', NULL, '2024-05-15 12:39:16.425', NULL, '2024-05-15 12:39:16.425', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1800332127544573952, '应用类型', 'APPLICATION_TYPE', '0', NULL, '2024-06-11 09:04:48.328', NULL, '2024-06-11 09:04:48.328', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1800332127544573955, '应用场景', 'APPLICATION_SCENE', '0', NULL, '2024-06-11 16:30:58.284', NULL, '2024-06-11 16:30:58.284', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1806606876608319489, '模型归属', 'MODEL_BELONG', '0', NULL, '2024-06-28 16:49:42.926', NULL, '2024-06-28 16:49:42.926', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1807951382385549312, '模型体验形式', 'MODEL_EXPERIENCE_SHAPE', '0', NULL, '2024-07-02 09:36:58.547', NULL, '2024-07-02 09:36:58.547', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1808053568557809664, '模型训练类型', 'MODEL_TRAIN_TYPE', '0', NULL, '2024-07-02 16:21:22.229', NULL, '2024-07-02 16:21:22.229', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1807951382385549315, '部署任务状态', 'DEPLOY_TASK_STATUS', '0', NULL, '2024-07-10 10:54:47.511', NULL, '2024-07-10 10:54:47.511', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1813040121084506112, '应用归属', 'APPLICATION_BELONG', '0', NULL, '2024-07-16 10:37:27.345', NULL, '2024-07-16 10:37:27.345', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1820390389576957952, '是否', 'YSE_OR_NO', '0', NULL, '2024-08-05 17:25:07.783', NULL, '2024-08-05 17:25:07.783', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1820390389576957953, '用户角色', 'USER_ROLE', '0', NULL, '2024-08-05 17:26:45.333', NULL, '2024-08-05 17:26:45.333', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1824280846832140289, '数据集类型', 'DATA_SET_TYPE', '0', NULL, '2024-08-16 11:15:10.856', NULL, '2024-08-16 11:15:10.856', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1824280846832140290, '数据集数据类型', 'DATA_SET_DATA_TYPE', '0', NULL, '2024-08-16 11:15:10.856', NULL, '2024-08-16 11:15:10.856', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1831575530340962304, '模型评估C-EVAL状态', 'MODEL_C_EVAL_STATUS', '0', NULL, '2024-09-05 14:12:51.333', NULL, '2024-09-05 14:12:51.333', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1824280846832140291, '数据集评估状态', 'DATA_SET_EVALUATION', '0', NULL, '2024-08-16 11:15:10.856', NULL, '2024-08-16 11:15:10.856', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1832984485743140864, '训推目标', 'TRAIN_DEPLOY_TARGET', '0', NULL, '2024-09-09 11:30:32.594', NULL, '2024-09-09 11:30:32.594', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1824280846832140288, '预测时长', 'PREDICTION_DURATION', '0', NULL, '2024-09-12 07:30:11.775012', NULL, '2024-09-12 07:30:11.775012', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1849008315963822083, '模型训练分类', 'MODEL_TRAIN_CLASSIFY', '0', NULL, '2024-11-04 17:41:37.732975', NULL, '2024-11-04 17:41:37.732975', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952576, '模型类型', 'MODEL_TYPE', '0', NULL, '2024-11-19 11:32:00.848', NULL, '2024-11-19 11:32:00.848', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952577, '模型权限用途', 'MODEL_AUTH_USAGE', '0', NULL, '2024-11-19 11:35:47.541', NULL, '2024-11-19 11:35:47.541', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952578, '工单审核状态', 'ORDER_REVIEW_STATUS', '0', NULL, '2024-12-06 15:44:52.786', NULL, '2024-12-06 15:44:52.786', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952588, '多模态检查细项', 'MULTI_MODAL_TASK', '0', NULL, '2024-12-06 15:44:52.786', NULL, '2024-12-06 15:44:52.786', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952589, '多模态检查项', 'MULTI_MODAL_ACTION', '0', NULL, '2024-12-06 15:44:52.786', NULL, '2024-12-06 15:44:52.786', NULL, NULL);
INSERT INTO meta.mm_dict_type VALUES (1858715343100952600, '多模态检查细项', 'MULTI_MODAL_TASK_MAP', '0', NULL, '2024-12-06 15:44:52.786', NULL, '2024-12-06 15:44:52.786', NULL, NULL);


--
-- TOC entry 3845 (class 0 OID 306742)
-- Dependencies: 240
-- Data for Name: mm_domain; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3846 (class 0 OID 306747)
-- Dependencies: 241
-- Data for Name: mm_group; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3847 (class 0 OID 306753)
-- Dependencies: 242
-- Data for Name: mm_group_user; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3887 (class 0 OID 379569)
-- Dependencies: 282
-- Data for Name: mm_host; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3848 (class 0 OID 306758)
-- Dependencies: 243
-- Data for Name: mm_model; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_model VALUES (1807586796071366656, 'Time-LLM', '1', 'time-llm', 'http://192.168.0.1:17000/TimeLLM', '0', NULL, NULL, NULL, '1', '1', '0', '1', '1', '1', '时序大横型可洞察到时间序列数据的规律，对未来趋势、模式、行为等进行分析', 0, '2024-07-01 10:32:10.923', 0, '2024-11-19 11:35:47.541', '123456', 'graph', 'GZ', 1, '2', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1823985532368408579, 'Qiming 1.5B', '1', 'qwen2_1.5b', 'http://192.168.0.1:20018/qiming1d5b/v1/chat/completions', '0', '', NULL, 1000, '1', '0', '0', '1', '0', '1', '支持更广泛的语言类型，支持32768个tokens文本处理能力', 0, '2024-08-15 15:55:24.128', 0, '2024-11-19 11:35:47.541', NULL, 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1790249036662829056, 'Qiming 14B', '1', 'qwen1.5_14b', 'http://192.168.0.1:20018/qiming14b/v1/chat/completions', '0', '', NULL, 1000, '1', '0', '0', '0', '0', '1', '140亿参数大模型，支持中英文输入，最长支持8192token，具备优秀的文本生成能力', 0, '2024-06-28 16:47:59.495', 0, '2024-11-19 11:35:47.541', '123456', 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1823985532368408581, 'Qiming 72B', '1', 'qwen2_72b', 'http://192.168.0.1:20018/qiming72b/v1/chat/completions', '1', NULL, NULL, 1000, '0', '0', '0', '1', '1', '1', '720亿参数大模型，学习海量通用数据、网络专业数据，支持中英文输入，最长支持8192token', 0, '2024-08-15 15:55:24.367', 0, '2024-11-22 17:00:54.306', NULL, 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1823985532368408580, 'Qiming 72B新', '1', 'qwen2_72b', 'http://192.168.0.1:38080/v1/chat/completions', '0', NULL, NULL, 1000, '0', '0', '0', '1', '1', '1', '720亿参数大模型，学习海量通用数据、网络专业数据，支持中英文输入，最长支持8192token', 0, '2024-08-15 15:55:24.367', 0, '2024-11-22 17:00:54.306694', NULL, 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, 'qiming25_72b');
INSERT INTO meta.mm_model VALUES (1823985532368408582, 'Qiming 14B新', '1', 'qwen2.5_14b', NULL, '0', NULL, NULL, 1000, '1', '0', '1', '1', '1', '1', NULL, 0, '2025-06-12 18:10:29.593183', 0, '2025-06-12 18:10:29.593183', NULL, 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1807586796075560962, '领域大模型', '1', 'domain', '', '0', NULL, NULL, NULL, '1', '1', '1', '1', '1', '1', '720亿参数大模型，学习海量通用数据、网络专业数据，支持中英文输入，最长支持8192token', 0, '2024-07-01 10:32:11.143', 0, '2024-11-19 11:35:47.541', '123456', NULL, 'GZ', 1, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1807586796075560961, '多模态大模型', '1', 'multi', '', '0', NULL, NULL, NULL, '1', '1', '1', '1', '1', '1', '多横态大横型可处理多种煤体数据，包括文本、图像、音频、视频等', 0, '2024-07-01 10:32:11.069', 0, '2024-11-19 11:35:47.541', '123456', NULL, 'GZ', 1, '3', '911010000000000000000000', NULL, NULL, NULL, NULL);
INSERT INTO meta.mm_model VALUES (1807586796075560960, 'Qiming 7B', '1', 'qwen2_7b', 'http://192.168.0.1:20018/qiming7b/v1/chat/completions', '0', '', NULL, 1000, '1', '0', '0', '0', '0', '1', '70亿参数大模型，可应用于意图识别等能力构建', 0, '2024-07-01 10:32:10.995', 0, '2024-11-19 11:35:47.541', '123456', 'QandA', 'GZ', 3, '1', '911010000000000000000000', NULL, NULL, NULL, NULL);


--
-- TOC entry 3849 (class 0 OID 306772)
-- Dependencies: 244
-- Data for Name: mm_model_train; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_model_train VALUES (1823985532368408588, 1807586796075560960, '2', 'LORA', 0, '2024-08-15 16:54:59.541', 0, '2024-08-15 16:54:59.541', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1823985532368408589, 1807586796075560960, '3', 'QLORA', 0, '2024-08-15 16:55:01.926', 0, '2024-08-15 16:55:01.926', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1823985532368408590, 1823985532368408579, '1', 'SFT', 0, '2024-08-15 16:55:04.81', 0, '2024-08-15 16:55:04.81', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1831888452921430016, 1823985532368408579, '2', 'LORA', 0, '2024-09-06 10:54:50.087', 0, '2024-09-06 10:54:50.087', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1831888452921430017, 1823985532368408579, '3', 'QLORA', 0, '2024-09-06 10:54:50.177', 0, '2024-09-06 10:54:50.177', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1831888452921430018, 1790249036662829056, '2', 'LORA', 0, '2024-09-06 11:07:46.475', 0, '2024-09-06 11:07:46.475', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1849008315968016409, NULL, '2', 'RL-LORA', 0, '2024-10-23 18:19:15.228', 0, '2024-10-23 18:19:15.228', 'normal');
INSERT INTO meta.mm_model_train VALUES (1859874334392803352, 1823985532368408580, '2', 'LORA', 0, '2024-11-22 16:24:03.728', 0, '2024-11-22 16:24:03.728', 'specifical');
INSERT INTO meta.mm_model_train VALUES (1859874334392803353, 1823985532368408582, '2', 'IR', 0, '2025-06-12 18:29:45.466967', 0, '2025-06-12 18:29:45.466967', 'specifical');


--
-- TOC entry 3850 (class 0 OID 306777)
-- Dependencies: 245
-- Data for Name: mm_model_train_param; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_model_train_param VALUES (1825395421510758400, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-08-21 18:15:38.808674', 0, '2024-11-08 17:18:52.52246', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758409, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-08-21 18:15:44.04831', 0, '2024-11-08 17:18:52.52246', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758418, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-08-21 18:15:44.824222', 0, '2024-11-08 17:18:52.52246', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758404, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-08-21 18:15:39.158523', 0, '2024-11-08 17:18:52.958288', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758413, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-08-21 18:15:44.398294', 0, '2024-11-08 17:18:52.958288', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758422, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-08-21 18:15:45.153331', 0, '2024-11-08 17:18:52.958288', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758401, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-08-21 18:15:38.899197', 0, '2024-11-08 17:18:52.575962', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758410, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-08-21 18:15:44.138141', 0, '2024-11-08 17:18:52.575962', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758419, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-08-21 18:15:44.908101', 0, '2024-11-08 17:18:52.575962', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758402, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-08-21 18:15:38.988242', 0, '2024-11-08 17:18:52.637695', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758411, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-08-21 18:15:44.231278', 0, '2024-11-08 17:18:52.637695', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758403, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-08-21 18:15:39.065343', 0, '2024-11-08 17:18:52.686986', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758412, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-08-21 18:15:44.320293', 0, '2024-11-08 17:18:52.686986', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758405, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-08-21 18:15:39.246049', 0, '2024-11-08 17:18:52.74228', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758414, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-08-21 18:15:44.476357', 0, '2024-11-08 17:18:52.74228', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758423, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-08-21 18:15:45.238269', 0, '2024-11-08 17:18:52.74228', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758406, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-08-21 18:15:39.338155', 0, '2024-11-08 17:18:52.792018', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758415, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-08-21 18:15:44.568271', 0, '2024-11-08 17:18:52.792018', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758424, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-08-21 18:15:45.318092', 0, '2024-11-08 17:18:52.792018', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758408, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'int', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-08-21 18:15:39.49425', 0, '2024-11-08 17:18:52.843305', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758417, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'int', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-08-21 18:15:44.747978', 0, '2024-11-08 17:18:52.843305', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758407, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-08-21 18:15:39.415417', 0, '2024-11-08 17:18:52.903449', 1823985532368408588);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758416, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-08-21 18:15:44.658198', 0, '2024-11-08 17:18:52.903449', 1823985532368408589);
INSERT INTO meta.mm_model_train_param VALUES (1831888452921430022, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.52246', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758425, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-08-21 18:15:45.39814', 0, '2024-11-08 17:18:52.903449', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624325, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.958288', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624326, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.52246', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207232, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.52246', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452921430023, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.575962', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624327, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.575962', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207233, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.575962', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452921430024, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.637695', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624328, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.637695', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624320, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.686986', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624329, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.686986', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624321, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.74228', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624330, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.74228', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207236, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.74228', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624322, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.792018', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624331, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.792018', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1825395421531729920, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'int', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-08-21 18:15:45.488158', 0, '2024-11-08 17:18:52.843305', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624323, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.843305', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624332, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.843305', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624324, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.903449', 1808424623155826690);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624333, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.903449', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452925624334, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.958288', 1831888452921430016);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401539, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.52246', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1849038333188341760, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-10-23 18:40:35.858', 0, '2024-11-08 17:18:52.52246', 1849008315968016409);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401540, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.575962', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758420, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-08-21 18:15:44.988225', 0, '2024-11-08 17:18:52.637695', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207234, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.637695', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401541, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.637695', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1825395421510758421, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-08-21 18:15:45.078124', 0, '2024-11-08 17:18:52.686986', 1823985532368408590);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207235, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.686986', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401542, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.686986', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401543, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.74228', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1849038333188341764, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-10-23 18:40:35.858', 0, '2024-11-08 17:18:52.74228', 1849008315968016409);
INSERT INTO meta.mm_model_train_param VALUES (1831888452938207237, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.792018', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401544, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.792018', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1849038333188341765, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-10-23 18:40:35.858', 0, '2024-11-08 17:18:52.792018', 1849008315968016409);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401536, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.843305', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401545, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.843305', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401537, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.903449', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401546, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.903449', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401538, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.958288', 1831888452921430017);
INSERT INTO meta.mm_model_train_param VALUES (1831888452942401547, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.958288', 1831888452921430018);
INSERT INTO meta.mm_model_train_param VALUES (1849038333188341768, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-10-23 18:40:35.858', 0, '2024-11-08 17:18:52.958288', 1849008315968016409);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803341, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803342, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803343, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803344, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803345, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803346, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803347, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803348, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803349, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-11-22 16:29:10.966', 0, '2024-11-22 16:29:10.966', 1859874334392803352);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803350, 'epochs', 'num_train_epochs', 'numTrainEpochs', 'int', NULL, 'range', '[1,5]', 1.0000000000, '1', '完整将数据集训练的次数，eg.epochs设置为2时，问答对会被学习2次，设置过大会可能导致模型出现灾难性遗忘问题', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.522', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803351, '学习率', 'learning_rate', 'learningRate', 'float', NULL, 'range', '[0.0001,0.001]', 0.0001000000, '0.0001', '决定模型更新权重的幅度，数值过大可能错过细节，反之则效率低下', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.575', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803352, 'batch_size', 'per_device_train_batch_size', 'perDeviceTrainBatchSize', 'int', NULL, 'range', '[1,4]', 1.0000000000, '2', '模型训练将数据分成小批处理，此参数用于设置每批训练样本数，大批次可加快训练但增加显存消耗', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.637', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803353, '梯度累积步数', 'gradient_accumulation_steps', 'gradientAccumulationSteps', 'int', NULL, 'range', '[1,64]', 1.0000000000, '1', '增大此数会增加显存消耗与训练速度，但也可能降低训练效果上限。该参数设置每处理几批（batch）数据后更新参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.686', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803354, '序列长度', 'cutoff_len', 'cutoffLen', 'int', NULL, 'range', '[1,1024]', 1.0000000000, '256', '单个问答对的最大input长度限制，超过此长度的问答对部分会被截去，请根据实际业务场景输入模型token数设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.742', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803355, '数据集样本数量上限', 'max_samples', 'maxSamples', 'int', NULL, 'range', '[1000,99999999]', 1.0000000000, '1000', '请根据待训练数据集问答对数量设置该参数', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.792', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803356, 'deepspeed策略选择', 'strategy_deepspeed', 'strategyDeepspeed', 'string', NULL, 'choice', '["1","2"]', 0.0000000000, '1', '选择不同的DeepSpeed优化级别，ZeRO-1通常更快但更耗显存。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.843', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803357, '分布式训练策略选择', 'strategy_distributed', 'strategyDistributed', 'boolean', NULL, 'choice', '["True","False"]', 0.0000000000, 'False', '标志是否使用多机多卡的分布式训练策略，可提高训练速度，模型尺寸较大，建议选择true', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.903', 1859874334392803353);
INSERT INTO meta.mm_model_train_param VALUES (1859874334392803358, '学习率预热比例', 'warmup_ratio', 'warmupRatio', 'float', NULL, 'range', '[0,1]', 0.0000100000, '0.1', '开始训练时采用较小学习率，逐步恢复正常值，有助于模型稳定及更快收敛。', 0, '2024-09-06 11:14:35.858', 0, '2024-11-08 17:18:52.958', 1859874334392803353);


--
-- TOC entry 3851 (class 0 OID 306786)
-- Dependencies: 246
-- Data for Name: mm_order; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3852 (class 0 OID 306793)
-- Dependencies: 247
-- Data for Name: mm_order_node; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3853 (class 0 OID 306798)
-- Dependencies: 248
-- Data for Name: mm_order_user; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3886 (class 0 OID 379560)
-- Dependencies: 281
-- Data for Name: mm_pod_detail; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3854 (class 0 OID 306804)
-- Dependencies: 249
-- Data for Name: mm_pr_test_set_evaluation; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3855 (class 0 OID 306812)
-- Dependencies: 250
-- Data for Name: mm_pr_test_set_evaluation_detail; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3856 (class 0 OID 306819)
-- Dependencies: 251
-- Data for Name: mm_pr_test_set_evaluation_score; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3885 (class 0 OID 379548)
-- Dependencies: 280
-- Data for Name: mm_project_host_rel; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3880 (class 0 OID 327394)
-- Dependencies: 275
-- Data for Name: mm_project_space; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3881 (class 0 OID 327402)
-- Dependencies: 276
-- Data for Name: mm_project_user; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3882 (class 0 OID 327409)
-- Dependencies: 277
-- Data for Name: mm_project_user_role; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3857 (class 0 OID 306823)
-- Dependencies: 252
-- Data for Name: mm_prompt_category_detail; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3858 (class 0 OID 306831)
-- Dependencies: 253
-- Data for Name: mm_prompt_resp; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3859 (class 0 OID 306840)
-- Dependencies: 254
-- Data for Name: mm_prompt_response_detail; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3860 (class 0 OID 306848)
-- Dependencies: 255
-- Data for Name: mm_prompt_sequential_detail; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3861 (class 0 OID 306855)
-- Dependencies: 256
-- Data for Name: mm_prompt_templates; Type: TABLE DATA; Schema: meta; Owner: -
--



--
-- TOC entry 3862 (class 0 OID 306862)
-- Dependencies: 257
-- Data for Name: mm_prompts; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_prompts VALUES (1849651679225020416, '你是主任，我希望你回答对员工的转岗问题${request}', 1818114331005214720, '2024-10-25 11:18:19.851', 1818114331005214720, '2024-10-25 11:18:19.851', -1, 'test', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1849714126241759232, '你会讲故事', 1744989376018288640, '2024-10-25 15:26:28.379', 1744989376018288640, '2024-10-25 15:26:28.379', -1, '1111', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1849720932963811328, '网络模型', 1744989376018288640, '2024-10-25 15:53:31.228', 1744989376018288640, '2024-10-25 15:53:31.228', -1, '1111', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1849265817584762880, '作为变更操作稽核助手，操作执行人和操作审核人不能为同一人，操作执行人和业务测试人不能为同一人，操作审核人可以是业务测试人，根据以上内容输出是否合规', 1759391970724847616, '2024-10-24 09:45:03.273', 1759391970724847616, '2024-10-28 09:53:44.266', -1, '变更操作工单助手测试', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1850720802260484096, '“在变更操作稽核中，操作执行人与操作审核人不得为同一人；同时，业务测试人不得担任操作执行人，但可以担任操作审核人，以确保合规性。', 1759391970724847616, '2024-10-28 10:06:38.646', 1759391970724847616, '2024-10-28 10:06:38.646', -1, '优化助手', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1850813326853505024, '作为变更操作稽核助手，操作执行人和审核人不能为同一人，操作执行人和测试人不能为同一人，审核人可以是测试人，根据以上内容输出是否合规', 1749606042716606464, '2024-10-28 16:14:18.227', 1749606042716606464, '2024-10-28 16:14:18.227', -1, '变更操作稽核助手', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1848988837505236992, '作为变更操作工单稽核助手，需遵循以下要求，要求一：操作执行人和操作审核人不能为同一人，操作执行人和业务测试人不能为同一人，操作审核人和业务测试人可以是同一人。要求二：操作批复时间与操作申请时间一致。满足以上两个要求即为合规操作。', 1759391970724847616, '2024-10-23 15:24:26.069', 1759391970724847616, '2024-10-23 15:41:53.109', -1, '四级及以上变更操作工单稽核助手', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1851102305335214080, '你是一个网络运维专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1850778617478569984, '2024-10-29 11:22:36.067', 1850778617478569984, '2024-10-29 11:26:13.531', -1, '测试1Prompt', '3', '-1', '2', 'request', NULL, '431102000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1848909318983352320, '作为变更操作稽核助手，要求：操作执行人和操作审核人不能为同一人，操作执行人和业务测试人不能为同一人，操作审核人和业务测试人可以为同一人，满足要求即为合规操作。根据以上内容对下文进行判别是否合规
', 1759391970724847616, '2024-10-23 10:08:27.39', 1759391970724847616, '2024-10-23 15:56:05.264', -1, '变更操作工单稽核助手', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1849343116669714432, '作为一个知识助手，请列举出与{type}}相关的规章制度', 1831140792429654016, '2024-10-24 14:52:12.805', 1831140792429654016, '2024-10-24 14:52:12.805', -1, 'test1024', '', '-1', '2', '', NULL, '111000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1849617090339241984, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1831140792429654016, '2024-10-25 09:00:53.219', 1831140792429654016, '2024-10-25 09:00:53.219', -1, '1025', '3', '-1', '2', 'type', NULL, '111000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1849644407975411712, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1732234610815643648, '2024-10-25 10:49:26.255', 1732234610815643648, '2024-10-25 10:49:26.255', -1, '测试模板1', '3', '-1', '2', 'type', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1782976821848268800, '请详细解释与{{context}}相关的所有安全规定和条例。', -1, '2024-04-24 11:36:28.633677', -1, '2024-05-28 07:18:08.051819', -1, '安全百问', '3', '1', '1', 'context', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1849651139485204480, '你是一个云资源运维专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1767440715543597056, '2024-10-25 11:16:11.167', 1767440715543597056, '2024-10-25 11:16:11.167', -1, 'prompt_test', '3', '-1', '2', 'request', NULL, '461000000000000000026357');
INSERT INTO meta.mm_prompts VALUES (1851082357804204032, '你是一个网络运维专家，请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1742363512897720320, '2024-10-29 10:03:20.211', 1742363512897720320, '2024-10-29 10:07:24.543', -1, 'nmg_prompt01', '3', '-1', '2', 'type', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1851088269616971776, '你是无线网优助手，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1769564800823132160, '2024-10-29 10:26:49.69', 1769564800823132160, '2024-10-29 10:26:49.69', -1, '测试1', '3', '-1', '2', 'request', NULL, '371110000000000000000021');
INSERT INTO meta.mm_prompts VALUES (1851099758490910720, '你是中国电信启明网络大模型，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1782686712461365248, '2024-10-29 11:12:28.852', 1782686712461365248, '2024-10-29 11:12:28.852', -1, 'lbb测试', '3', '-1', '2', 'request', NULL, '121000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1851103698699780096, '您是一位专业的中国电信变更操作管理员，我们希望您能对下列问题进行详细且全面的回答：{{question}} ', 1850779816837234688, '2024-10-29 11:28:08.271', 1850779816837234688, '2024-10-29 11:28:08.271', -1, '变更测试', '3', '-1', '2', 'question', NULL, '641102000000000000000069');
INSERT INTO meta.mm_prompts VALUES (1851106166938632192, '你是传输专业网络故障处置专家，当前传输故障为{{context}}，请生成专业处置方案解决传输故障。', 1782675351152988160, '2024-10-29 11:37:56.745', 1782675351152988160, '2024-10-29 11:37:56.745', -1, '张志志', '3', '-1', '2', 'context', NULL, '121000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1851108855709794304, '你是一个网络运维专家，请对以下问题进行回答：
${request}', 1850785192003444736, '2024-10-29 11:48:37.797', 1850785192003444736, '2024-10-29 11:48:37.797', -1, 'test', '', '-1', '2', '', NULL, '341033000000000002392001');
INSERT INTO meta.mm_prompts VALUES (1851135955376373760, '作为一个AI助手，请尽量回答如下问题：{{question}}', 1851120403616993280, '2024-10-29 13:36:18.862', 1851120403616993280, '2024-10-29 13:37:20.556', -1, 'ah_wx_1', '3', '-1', '2', 'question', NULL, '341033000000000002392001');
INSERT INTO meta.mm_prompts VALUES (1851147532473303040, '网络资源基础知识助手 Prompt 模板

章节请求

请求内容：请提供关于“[章节名称]”的详细信息。
示例：请提供关于“需求管理基本概念”的详细信息。
问题解答

请求内容：请回答关于“[具体问题]”的问题。
示例：请解释什么是需求获取。
总结请求

请求内容：请对“[章节名称]”进行100字或50字的总结。
示例：请对“资源可视化”进行100字的总结。
知识应用

请求内容：请提供如何将“[知识点]”应用于实际管理中的建议。
示例：请提供如何将“资源动态维护”应用于实际管理中的建议。
案例分析

请求内容：请提供关于“[知识点]”的实际案例分析。
示例：请提供关于“需求审核”的实际案例分析。
对比分析

请求内容：请对比“[知识点1]”和“[知识点2]”的异同。
示例：请对比“资源树”和“宽表结构”的异同。', 1768540857521373184, '2024-10-29 14:22:19.056', 1768540857521373184, '2024-10-29 14:22:19.056', -1, '网络资源基础知识助手', '', '-1', '2', '', NULL, '311115000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1851150978622521344, '你是一名网络运维专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1851099207568760832, '2024-10-29 14:36:00.683', 1851099207568760832, '2024-10-29 14:36:00.683', -1, '测试1', '3', '-1', '2', 'request', NULL, '341033000000000002392001');
INSERT INTO meta.mm_prompts VALUES (1851156663783489536, '作为一个知识助手，请详细回答这个问题{{context}}', 1844665743870308352, '2024-10-29 14:58:36.13', 1844665743870308352, '2024-10-29 14:58:36.13', -1, 'ip问答模板', '3', '-1', '2', 'context', NULL, '431102000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1782975869288275968, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', -1, '2024-04-24 11:32:41.497342', -1, '2024-05-28 07:18:08.051819', -1, '规章制度问答', '3', '1', '1', 'type', 'http://xxxxxx', NULL);
INSERT INTO meta.mm_prompts VALUES (1851975988601782272, '了解了，作为启明网络大模型，我将遵循上述设定，以人力晋岗晋级咨询机器人的身份，为用户提供专业且高效的咨询服务。请告诉我您的具体问题或情况，我将竭诚为您解答。', 1810574772478595072, '2024-10-31 21:14:18.389', 1810574772478595072, '2024-10-31 21:14:18.389', -1, '晋级晋岗咨询机器人', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1856175154771009536, '公文写作助手 Prompt
欢迎信息
欢迎使用公文写作助手！我是您的智能辅助工具，旨在帮助您高效、准确地完成各类公文撰写工作。无论您是需要起草通知、报告、请示、批复还是会议纪要等，我都可以提供相应的指导和支持。
功能介绍
文档类型选择：请告知您需要撰写的公文类型（如：通知、报告、请示、批复、会议纪要等）。
基本信息填写：
标题：请提供公文的标题。
收件人/部门：请指定接收该公文的对象或部门。
发件人/部门：请确认发送该公文的个人或部门。
日期：请指定公文的成文日期。
正文内容指导：
目的与背景：简述公文撰写的目的及相关的背景信息。
具体内容：详细描述公文的主要内容，包括但不限于关键数据、事实陈述、意见表达等。
要求与建议：如有任何具体的要求或建议，请在此部分说明。
结束语：请确定是否需要标准的结束语或有特定的结束方式。
附件说明：如果公文附带文件，请提供附件的名称及简要说明。
使用指南
在输入您的需求时，请尽量详尽，这将有助于我更准确地为您提供帮助。
我会根据您的输入生成初稿，之后您可以根据实际情况进行修改和完善。
如在使用过程中遇到任何问题，或需要进一步的帮助，请随时告诉我。
', 1820365093594509312, '2024-11-12 11:20:17.621', 1820365093594509312, '2024-11-12 11:20:23.506', -1, '公文写作助手', '', '-1', '2', '', NULL, '431102000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1857250721255661568, '您是一位专业的中国电信家宽装维专家，我们希望您能对下列问题进行详细且全面处理并生成对应操作流程：{{Request}} 针对上述问题，我们为您提供了下列参考信息：{{KNOWLEDGE}} 
. 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 基于参考信息，提供具体、明确且易于理解的答案。3. 不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', 1674367441832968192, '2024-11-15 10:34:12.655', 1674367441832968192, '2024-11-15 10:34:12.655', -1, 'testzzprompt', '3', '-1', '2', 'Request,KNOWLEDGE', NULL, '521000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1857259708743163904, '您是一位专业的中国电信移动网专家，我们希望您能对下列问题进行详细且全面的回答：{{question}} 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 提供具体、明确且易于理解的答案。3. 确保所有提供的信息都是基于现有的知识和事实，不要捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。
', 1742742240778162176, '2024-11-15 11:09:55.438', 1742742240778162176, '2024-11-15 11:09:55.438', -1, '移动网发展趋势问答', '3', '-1', '2', 'question', NULL, '141011000000000000049839');
INSERT INTO meta.mm_prompts VALUES (1857260744916606976, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1743148966153748480, '2024-11-15 11:14:02.481', 1743148966153748480, '2024-11-15 11:14:13.833', -1, '测试1', '3', '-1', '2', 'type', NULL, '521000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1857261093115142144, '请详细解释与{{context}}相关的所有安全规定和条例。', 1777501031577882624, '2024-11-15 11:15:25.498', 1777501031577882624, '2024-11-15 11:15:25.498', -1, 'Prompt_CS_GSLJF', '3', '-1', '2', 'context', NULL, '621102000900800000004455');
INSERT INTO meta.mm_prompts VALUES (1859858214298914816, '你是{{major}}专业问答助手，请参考以下知识回答用户故障问题，知识{{knowledge}}，用户问题：{{question}}', 1668426104841285632, '2024-11-22 15:15:27.447', 1668426104841285632, '2024-11-22 15:53:48.465', -1, '智能语音云', '3', '-1', '2', 'major,knowledge,question', NULL, '621102000900800000004455');
INSERT INTO meta.mm_prompts VALUES (1859484009602551808, '作为一个智障，你应该尽可能对用户的问题假装不清楚，胡说八道地回答他。', 1859401666236878848, '2024-11-21 14:28:30.118', 1859401666236878848, '2024-11-21 14:30:47.645', -1, '智障模板', '', '-1', '2', '', NULL, '441000000000000103054855');
INSERT INTO meta.mm_prompts VALUES (1859581363970183168, '河源电信AI人工智能学习测试', 1831226849332482048, '2024-11-21 20:55:21.178', 1831226849332482048, '2024-11-21 20:55:21.178', -1, '河源电信AI人工智能学习', '', '-1', '2', '', NULL, '441000000000000103054855');
INSERT INTO meta.mm_prompts VALUES (1859790825549008896, '请列举出资源实体{{type}}相关的命名规范 。', 1776897888185417728, '2024-11-22 10:47:40.711', 1776897888185417728, '2024-11-22 10:47:40.711', -1, '命名规范', '3', '-1', '2', 'type', NULL, '441000000000000103054855');
INSERT INTO meta.mm_prompts VALUES (1860947938748993536, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1782345234544123904, '2024-11-25 15:25:38.025', 1782345234544123904, '2024-11-25 15:25:38.025', -1, '投诉工单', '3', '-1', '2', 'content', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1861364936507199488, '作为一个无线网络优化专家，请列举出产生{{content}}问题的原因', 1858458514668318720, '2024-11-26 19:02:38.018', 1858458514668318720, '2024-11-26 19:02:38.018', -1, '无忧无虑', '3', '-1', '2', 'content', NULL, '321122930000000000000014');
INSERT INTO meta.mm_prompts VALUES (1861379142228971520, '你是一名经验丰富的居民区射频优化专家，希望你能够通过引导获得居民区楼宇数量、楼宇高度等{{KNOWLEDGE}}，结合你的居民区优化经验，对{{request}}居民区内射频天线排布位置、天线选型、下倾角和方位角设置等提出科学合理的优化建议。', 1858452474924470272, '2024-11-26 19:59:04.917', 1858452474924470272, '2024-11-26 19:59:04.917', -1, '居民区射频优化问答', '3', '-1', '2', 'KNOWLEDGE,request', NULL, '321122930000000000000014');
INSERT INTO meta.mm_prompts VALUES (1861687520440422400, 'aaa', 1860973273967333376, '2024-11-27 16:24:28.012', 1860973273967333376, '2024-11-27 16:24:28.012', -1, '测试Prompt', '', '-1', '2', '', NULL, '500000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1861705411760033792, '你好：机器猫为你服务', 1852236985907093504, '2024-11-27 17:35:33.634', 1852236985907093504, '2024-11-27 17:35:33.634', -1, '测试', '', '-1', '2', '', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1782991829479022592, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', -1, '2024-04-24 12:36:06.767155', -1, '2024-05-28 07:18:08.051819', -1, '投诉工单处置', '3', '10', '1', 'content', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1790643183672639488, '请训练11', 1736923672699998208, '2024-05-15 15:19:38.188331', 1736923672699998208, '2024-05-28 07:18:08.051819', -1, '训练11', NULL, '-1', '2', NULL, NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1852163694803910656, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1730034630945329152, '2024-11-01 09:40:11.036', 1730034630945329152, '2024-11-01 09:40:11.036', -1, '测试模板01', '3', '-1', '2', 'content', NULL, '511000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1852163748209983488, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1817747729611743232, '2024-11-01 09:40:23.768', 1817747729611743232, '2024-11-01 09:40:23.768', -1, '内蒙古_杨耀忠_12期', '3', '-1', '2', 'type', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1782976721579237376, '请整理公司{{type}}相关的规章制度，对应制度条例可能包括{{content}}，请总结并输出关联的内容，不要输出无关内容。', -1, '2024-04-24 11:36:04.727889', -1, '2024-05-28 07:18:08.051819', -1, '规章制度问答（RAG）', '3', '1', '1', 'type,content', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1852163752618196992, '您是一位专业的中国电信网络运维专家，我们希望您能对下列问题进行详细且全面的回答：{{question}} 
请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 提供具体、明确且易于理解的答案。3. 确保所有提供的信息都是基于现有的知识和事实，不要捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', 1669220647239344128, '2024-11-01 09:40:24.82', 1669220647239344128, '2024-11-01 09:40:24.82', -1, '测试prom', '3', '-1', '2', 'question', NULL, '361011000000000036696304');
INSERT INTO meta.mm_prompts VALUES (1852171272900411392, '你是中国电信启明网络大模型，精通无线网络优化领域的知识。请根据以下参考信息，回答以下问题： 参考信息：{{knowledge}} 问题：{{question}} 注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', 1785226419040784384, '2024-11-01 10:10:17.794', 1785226419040784384, '2024-11-01 10:10:17.794', -1, '江苏无线网优助手', '3', '-1', '2', 'knowledge,question', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1852173315115417600, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1731576138093236224, '2024-11-01 10:18:24.697', 1731576138093236224, '2024-11-01 10:24:09.821', -1, '广西卡单自愈知识助手模板', '3', '-1', '2', 'content', NULL, '451102000000000021333149');
INSERT INTO meta.mm_prompts VALUES (1852175802044088320, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1810574772478595072, '2024-11-01 10:28:17.627', 1810574772478595072, '2024-11-01 10:28:17.627', -1, '中国电信客服', '3', '-1', '2', 'content', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1852180911331311616, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1838050695620640768, '2024-11-01 10:48:35.776', 1838050695620640768, '2024-11-01 10:48:41.94', -1, 'QZWUJP提示模板测试', '3', '-1', '2', 'content', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1852183395516907520, '请详细解释与{{context}}相关的所有信息。', 1828705139891957760, '2024-11-01 10:58:28.051', 1828705139891957760, '2024-11-01 10:58:28.051', -1, '测试_陈锋泽', '3', '-1', '2', 'context', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1854906616919891968, '你是CPE验收小助手，现在接收到用户希望验收的设备名称为：{{objName}}，检查只子项为：{{checkSubitemIds}}，请根据验收标准完成对该设备检查项的验收，并针对具体内容生成详细验收及如果。', 1675704191108972544, '2024-11-08 23:19:34.667', 1675704191108972544, '2024-11-08 23:20:02.036', -1, 'OTN验收CPE对象', '3', '-1', '2', 'objName,checkSubitemIds', NULL, '141011000000000000049839');
INSERT INTO meta.mm_prompts VALUES (1852163792266952704, '你是中国电信启明网络大模型，精通电联4G一张网共建共享相关知识的解答，我们希望您能对下列问题进行详细且全面的回答： {{request}}', 1772071852652826624, '2024-11-01 09:40:34.272', 1772071852652826624, '2024-11-01 13:11:49.642', -1, '共建共享prompt', '3', '-1', '2', 'request', NULL, '451102000000000021333149');
INSERT INTO meta.mm_prompts VALUES (1852219271483588608, '你是中国电信启明网络大模型，电联4G一张网共建共享相关知识。请根据以下参考信息，回答以下问题： 参考信息：{{knowledge}} 问题：{{question}} 注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', 1772071852652826624, '2024-11-01 13:21:01.549', 1772071852652826624, '2024-11-01 13:21:01.549', -1, '电联4G一张网共建共享', '3', '-1', '2', 'knowledge,question', NULL, '451102000000000021333149');
INSERT INTO meta.mm_prompts VALUES (1852234500737171456, '你是中国电信客服，现在接收到用户的投诉工单内容为：开通工单自动环节流转异常,产品类型为{{product_type}},服务类型为{{service_type}},报错环节为{{error_api}},报错信息为{{error_message}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1731576138093236224, '2024-11-01 14:21:32.486', 1731576138093236224, '2024-11-01 14:21:32.486', -1, '广西业务卡单自愈助手提示词2', '3', '-1', '2', 'product_type,service_type,error_api,error_message', NULL, '451102000000000021333149');
INSERT INTO meta.mm_prompts VALUES (1852332882453430272, '你好，请根据{{context}}，和{{question}}，给出你的回答。', 1785226419040784384, '2024-11-01 20:52:28.522', 1785226419040784384, '2024-11-01 20:54:04.162', -1, 'jiawang_prompt', '3', '-1', '2', 'context,question', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1853616530645417984, '你好，我是广州数字人助手', 1848615236938076160, '2024-11-05 09:53:14.119', 1848615236938076160, '2024-11-05 09:53:14.119', -1, '广州数字人助手', '', '-1', '2', '', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1856164553214664704, '你是一个网络运维专家，我们希望您能对下列问题进行详细且全面的回答： 
${{request}}', 1770000747140263936, '2024-11-12 10:38:10.02', 1770000747140263936, '2024-11-12 10:38:10.02', -1, '测试prompt_1112', '3', '-1', '2', 'request', NULL, '621102000900800000004455');
INSERT INTO meta.mm_prompts VALUES (1856167748120879104, '你是一个大数据与AI专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1856137543804727296, '2024-11-12 10:50:51.739', 1856137543804727296, '2024-11-12 10:50:51.739', -1, '测试1_prompt', '3', '-1', '2', 'request', NULL, '461000000000000000026357');
INSERT INTO meta.mm_prompts VALUES (1856181773596631040, '你是中国电信启明网络大模型，我们希望你能回答以下问题：
${request}', 1745332620062023680, '2024-11-12 11:46:35.673', 1745332620062023680, '2024-11-12 11:46:35.673', -1, '十三期_swh_prompt', '', '-1', '2', '', NULL, '461000000000000000026357');
INSERT INTO meta.mm_prompts VALUES (1856229349008089088, '你是中国电信启明网络大模型，精通无线网络优化领域的知识。请根据以下参考信息，回答以下问题：
参考信息：{{knowledge}}
问题：{{question}}

注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', 1726509721851064320, '2024-11-12 14:55:38.535', 1726509721851064320, '2024-11-12 14:55:38.535', -1, '启明计划培训test', '3', '-1', '2', 'knowledge,question', NULL, '331711000000000021720883');
INSERT INTO meta.mm_prompts VALUES (1856231733675433984, '你是一个百科专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1856149603404386304, '2024-11-12 15:05:07.084', 1856149603404386304, '2024-11-12 15:25:54.464', -1, '测试g1prompt', '3', '-1', '2', 'request', NULL, '111000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1856497357932732416, 'demo_test=20', 1730469909749485568, '2024-11-13 08:40:36.859', 1730469909749485568, '2024-11-13 08:40:36.859', -1, 'jx_yfc_prompt', '', '-1', '2', '', NULL, '361011000000000036696304');
INSERT INTO meta.mm_prompts VALUES (1857226704033423360, '请详细解释与{{context}}相关的所有安全规定和条例，可参考以下知识内容{{knowledge}}。', 1701065831400792064, '2024-11-15 08:58:46.519', 1701065831400792064, '2024-11-15 08:58:46.519', -1, '我的测试prompt', '3', '-1', '2', 'context,knowledge', NULL, '461000000000000000026357');
INSERT INTO meta.mm_prompts VALUES (1834430034797494272, 'test', 1772163789678891008, '2024-09-13 11:12:57.014', 1772163789678891008, '2024-09-13 11:12:57.014', -1, 'test', '', '-1', '2', '', NULL, '631100000000000067126468');
INSERT INTO meta.mm_prompts VALUES (1857242806545293312, '你是中国电信客服，现在接收到用户的投诉工单内容为：{{content}}，请根据你已有经验分析该投诉工单内容，并针对具体内容生成处置方案。请详细说明应该有的处置流程。', 1820626192236408832, '2024-11-15 10:02:45.641', 1820626192236408832, '2024-11-15 10:02:45.641', -1, '测试模板', '3', '-1', '2', 'content', NULL, '311115000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1858692431902941184, '业务平台智能问答', 1829777324505907200, '2024-11-19 10:03:03.265', 1829777324505907200, '2024-11-19 10:03:03.265', -1, '业务平台智能问答', '', '-1', '2', '', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1782976891528241152, '请详细解释与{{context}}相关的所有安全规定和条例，可参考以下知识内容{{knowledge}}。', -1, '2024-04-24 11:36:45.215443', -1, '2024-05-28 07:18:08.051819', -1, '安全百问（+RAG）', '3', '1', '1', 'context,knowledge', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782987014178955264, '您是一位专业的中国电信网络运维专家，我们希望您能对下列问题进行详细且全面的回答：{{question}} 
请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 提供具体、明确且易于理解的答案。3. 确保所有提供的信息都是基于现有的知识和事实，不要捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', -1, '2024-04-24 12:16:58.665465', -1, '2024-05-28 07:18:08.051819', -1, '综维知识问答', '3', '2', '1', 'question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782987135184625664, '您是一位专业的中国电信网络运维专家，我们希望您能对下列问题进行详细且全面的回答：
 CONTEXT 
 针对上述问题，我们为您提供了下列参考信息：
 KNOWLEDGE 
. 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 基于参考信息，提供具体、明确且易于理解的答案。3. 不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', -1, '2024-04-24 12:17:27.515401', -1, '2024-05-28 07:18:08.051819', -1, '综维知识问答(+RAG)', '3', '2', '1', '', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782987296740827136, '作为{{province}}综维客服，你能根据用户问题获取本省综维领域知识，并结合所获取知识整理内容为用户提供满意的回复。当前用户问题为{{question}}，所获得的知识为{{knowledge}}，请准确回答客户问题，对应提解释说明。', -1, '2024-04-24 12:18:06.093216', -1, '2024-05-28 07:18:08.051819', -1, '综维知识客户服务', '3', '2', '1', 'province,question,knowledge', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782990894786764800, '你是中国电信启明网络大模型，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', -1, '2024-04-24 12:32:23.881557', -1, '2024-05-28 07:18:08.051819', -1, '无线网优知识问答', '3', '3', '1', 'request', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782990930199273472, '你是中国电信启明网络大模型，精通无线网络优化领域的知识。请根据以下参考信息，回答以下问题：
参考信息：{{knowledge}}
问题：{{question}}

注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', -1, '2024-04-24 12:32:32.325155', -1, '2024-05-28 07:18:08.051819', -1, '无线网优知识问答（+RAG）', '3', '3', '1', 'knowledge,question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782990959936888832, '您是一位专业的中国电信家宽装维知识专家，我们希望您能对下列问题进行详细且全面的回答：{{question}} 
, 请正常与用户进行聊天问答，对于知识请>教类的问题，请基于您的专业知识给出专业回答。', -1, '2024-04-24 12:32:39.41319', -1, '2024-05-28 07:18:08.051819', -1, '装维知识问答', '3', '4', '1', 'question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782990990358175744, '您是一位专业的中国电信家宽装维知识专家，我们希望您能对下列问题进行详细且全面的回答：{{CONTEXT}} 
 针对上述问题，我们为您提供了下列参考信息：
 {{KNOWLEDGE}} 
. 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 基于参考信息，提供具体、明确且易于理解的答案。不要使用其中没有帮助或与问题无关的参考信息。3. 不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', -1, '2024-04-24 12:32:46.667822', -1, '2024-05-28 07:18:08.051819', -1, '装维知识问答(+RAG)', '3', '4', '1', 'CONTEXT,KNOWLEDGE', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991790698487808, '你是一个TelePG使用专家，初级运维人员像你咨询TelePG相关操作方案。初级运维人员的问题是{{question}}，对应运维文档内容为{{knowledge}}，请参考运维文档内容，从专业角度回答问题。', -1, '2024-04-24 12:35:57.493757', -1, '2024-05-28 07:18:08.051819', -1, 'PaaS知识问答', '3', '9', '1', 'question,knowledge', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991041121837056, '请问该如何处理这条告警：故障的现象是{{FaultClass}}，告警信息是{{FaultDescription}}
请先指出此告警的具体故障，再以200字以内的篇幅分点分条地给出该告警可能的故障原因以及每个故障原因对应的解决方案，解决方案需要关键、合理，不同的故障原因对应的解决方案尽可能不重复。下面是一个回答的模板，供你参考：
具体故障为：xxx
1、可能的原因为xxx，对应的解决方案为：xxx
2、可能的原因为xxx，对应的解决方案为：xxx。', -1, '2024-04-24 12:32:58.771392', -1, '2024-05-28 07:18:08.051819', -1, '故障管理', '3', '5', '1', 'FaultClass,FaultDescription', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991072117743616, '你是{{major}}专业网络故障问答助手，请参考以下知识回答用户故障问题，知识{{knowledge}}，用户问题：{{question}}', -1, '2024-04-24 12:33:06.210752', -1, '2024-05-28 07:18:08.051819', -1, '分专业故障知识问答', '3', '5', '1', 'major,knowledge,question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991115688173568, '你是传输专业网络故障处置专家，当前传输故障为{{context}}，请生成专业处置方案解决传输故障。', -1, '2024-04-24 12:33:16.619527', -1, '2024-05-28 07:18:08.051819', -1, '传输故障处置方案生成', '3', '6', '1', 'context', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991149708173312, '你是传输专业网络故障处置专家，当前传输故障为{{context}}，可参考的历史处置方案为{{example}}，请生成专业处置方案解决传输故障。', -1, '2024-04-24 12:33:24.710246', -1, '2024-05-28 07:18:08.051819', -1, '传输故障处置方案生成（+RAG）', '3', '6', '1', 'context,example', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1847164661991571456, '你是中国电信启明网络大模型，精通5G核心网领域的知识。请根据以下参考信息，回答以下问题：
参考信息：{{knowledge}}
问题：{{question}}

注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', 1791295053287194624, '2024-10-18 14:35:48.736', 1791295053287194624, '2024-10-18 14:41:52.69', -1, '5Gtest', '3', '-1', '2', 'knowledge,question', NULL, '111000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1782991367208001536, '请根据高危指令信息和对应影响，生成高危指令稽核结果，提示用户所处理文档中涉及的所有高危指令。
Q:STR PAEFABRICTM
A:使用本命令后，部分Fabric链路可能会出现Down的情况，会导致报文丢失
Q:DEA MODULE
A:除了在开局初始配置时，其他情况下执行此命令将影响该模块的业务处理能力。
下面是一个回答的模板，供你参考：
高危指令共：XXX条，分别包括：1. XXX，对应产生的风险为：xxx
2、xxx，对应产生的风险为：xxx。', -1, '2024-04-24 12:34:16.541549', -1, '2024-05-28 07:18:08.051819', -1, '高危指令稽核', '3', '7', '1', '', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991733278466048, '你是一个工单信息审核员，当前所获得工单信息为：{{content}}，对应附件信息为{{file}}，请按工单关键信息，一个一个准确核对是否与附件信息一致，不一致请简要指出工单错误。', -1, '2024-04-24 12:35:43.794745', -1, '2024-05-28 07:18:08.051819', -1, '变更工单稽核', '3', '7', '1', 'content,file', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1858718151853219840, '作为装维故障处置决策助手，可以从问题的具体现象里面识别故障业务类型、故障现象、故障描述，深度解析业务类型+故障现象+故障描述详情相关内容，并输出故障原因预判，处理步骤，处理步骤分解为现场处置方法、智慧营维操作方法、客调支撑方法及安全注意要点等相关内容，不需要过渡解读和泛化，对文案中的人名、联系方式等内容屏蔽，不做解析输出。', 1745709758585700352, '2024-11-19 11:45:15.373', 1745709758585700352, '2024-11-28 14:52:34.776', -1, '装维故障处置决策助手', '', '-1', '2', '', NULL, '341033000000000002392001');
INSERT INTO meta.mm_prompts VALUES (1782991758842748928, '您是一位专业的中国电信家宽装维专家，我们希望您能对下列问题进行详细且全面处理并生成对应操作流程：{{Request}} 针对上述问题，我们为您提供了下列参考信息：{{KNOWLEDGE}} 
. 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 基于参考信息，提供具体、明确且易于理解的答案。3. 不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', -1, '2024-04-24 12:35:49.923818', -1, '2024-05-28 07:18:08.051819', -1, '家宽装维自服务', '3', '8', '1', 'Request,KNOWLEDGE', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991892540383232, '目前路由调整单用户诉求为：{{request}}，对应相似路由配置为{{config}}，请根据用户诉求一条一条替换相似配置中关键字段信息，并生成新的配置信息。', -1, '2024-04-24 12:36:21.766598', -1, '2024-05-28 07:18:08.051819', -1, '路由调整工单脚本生成', '3', '10', '1', 'request,config', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782991988556390400, '我有一组{{province}}的网络IP设备配置命令，对应设备型号为{{Devicetype.}}希望您帮我确认是否正确。以下是配置命令：{{confCode}},请问，以上配置命令是否正确？如果有任何建议或修改，请简要提示告诉我！', -1, '2024-04-24 12:36:44.657764', -1, '2024-05-28 07:18:08.051819', -1, 'IP设备配置稽核', '3', '11', '1', 'province,Devicetype.,confCode', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1782992039630430208, '请根据以下无线网络故障详细信息，生成一份无线故障处置方案。故障现象：{{symptom}}，影响范围：{{impact}}。请详细描述故障的原因，列出可能的解决方案，并说明每种方案的优缺点。最后，选择最佳的解决方案，并提供实施步骤和预期结果。', -1, '2024-04-24 12:36:56.834012', -1, '2024-05-28 07:18:08.051819', -1, '无线故障处置方案生成', '3', '12', '1', 'symptom,impact', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1787291446215720960, '您是一位专业的中国电信家宽装维知识专家，我们希望您能对下列问题进行详细且全面的回答：{{question}} 
, 请正常与用户进行聊天问答，对于知识请>教类的问题，请基于您的专业知识给出专业回答。', 100018, '2024-05-06 09:21:01.705753', 100018, '2024-05-28 07:18:08.051819', -1, '战新装维', '2', '-1', '2', 'question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1787379164023111680, '优化后的prompt模板： \n\"你好，{}。请问有什么我可以帮助您的？\"', 1736923672699998208, '2024-05-06 15:09:35.260375', 1736923672699998208, '2024-05-28 07:18:08.051819', -1, '优化后的prompt模板', '3', '-1', '2', '', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1798532084644007936, '请根据以下无线网络故障详细信息，生成一份无线故障处置方案。故障现象：{{symptom}}，影响范围：{{impact}}。请详细描述故障的原因，列出可能的解决方案，并说明每种方案的优缺点。最后，选择最佳的解决方案，并提供实施步骤和预期结果。', 1768567685062123520, '2024-06-06 09:47:18.764799', 1768567685062123520, '2024-06-06 09:47:18.764799', -1, 'test010', '3', '-1', '2', 'symptom,impact', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1800374040831545344, '你是一个黎平电信专家，拥有黎平本地网数据', 1745716492462837760, '2024-06-11 11:46:35.324929', 1745716492462837760, '2024-06-11 11:46:35.324929', -1, 'test4黎平', '', '-1', '2', '', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1810580755506089984, '你好，我是中国电信启明大模型，为您解答关于无线网优投诉的各类问题', 1760501879664136192, '2024-07-09 15:44:25.735801', 1760501879664136192, '2024-07-09 15:44:25.735801', -1, '投诉智能体', '', '-1', '2', '', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1815356766517022720, '作为一个运维专家，请回答{{value}}', 1732287464378253312, '2024-07-22 20:02:35.494632', 1732287464378253312, '2024-07-22 20:02:51.402058', -1, '测试', '3', '-1', '2', 'value', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1815360725084360704, '你是一个网络运维专家，我们希望您能对下列问题进行详细且全面的回答：
{{request}}', 1732287464378253312, '2024-07-22 20:18:19.291239', 1732287464378253312, '2024-07-22 20:18:19.291239', -1, '测试1prompt', '3', '-1', '2', 'request', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1817193154237517824, '你是一个规章专家，请整理公司{{type}}相关的规章制度，对应制度条例可能包括{{content}}，请总结并输出关联的内容，不要输出无关内容。', 1752241359936294912, '2024-07-27 21:39:44.469', 1752241359936294912, '2024-07-27 21:39:44.47', -1, '测试', '3', '-1', '2', 'type,content', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1818096387457409024, '于以下内容回答问题，要求表达结构清晰，内容简洁：
内容：
{{source_knowledge}}

问题：
{{question}}', 1663472232541257728, '2024-07-30 09:28:52.053', 1663472232541257728, '2024-07-30 09:28:52.054', -1, '陕西需求管理模板', '3', '-1', '2', 'source_knowledge,question', NULL, '611000000000001824854982');
INSERT INTO meta.mm_prompts VALUES (1818851793075036160, '作为一个知识助手，请列举出与{{type}}相关的规章制度。', 1811591858711085056, '2024-08-01 11:30:34.788', 1811591858711085056, '2024-08-01 11:30:34.789', -1, 'test', '3', '-1', '2', 'type', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1820987826117926912, '您是一位专业的海南公司的业务专家，我们希望您能对下列问题进行详细且全面的回答：
 CONTEXT 
 针对上述问题，我们为您提供了下列参考信息：
 KNOWLEDGE 
. 请您按照以下步骤进行思考：1. 从您的专业知识和经验中，进行深入地分析和解读。2. 基于参考信息，提供具体、明确且易于理解的答案。3. 不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。我们希望您的答案能够为用户提供准确、全面和深入的理解，帮助他们解决实际问题。', 1739818450576338944, '2024-08-07 08:58:24.753', 1739818450576338944, '2024-08-07 08:58:24.755', -1, '海南知识问答', '', '-1', '2', '', NULL, '461000000000000000026357');
INSERT INTO meta.mm_prompts VALUES (1809427728082489344, '请猎取与{{type}}', 1745638754026045440, '2024-07-06 11:22:42.653858', 1745638754026045440, '2024-08-07 15:28:30.433', -1, '测试', '3', '-1', '2', 'type', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1801544752279670784, '你是中国电信启明网络大模型，精通无线网络优化领域的知识。请根据以下参考信息，回答以下问题：
参考信息：{{knowledge}}
问题：{{question}}

注意，不要使用参考信息中的地点、人名等非关键信息进行回答，不要回答捏造或误报信息。如果您觉得参考信息没用，也可以不参考。', 1674257996518227968, '2024-06-14 17:18:34.686728', 1674257996518227968, '2024-08-22 09:58:14.661', -1, '河北测试_无线网优', '3', '-1', '2', 'knowledge,question', NULL, NULL);
INSERT INTO meta.mm_prompts VALUES (1826153938435510272, '请详细说明{{context}}相关的知识。', 1818179516122071040, '2024-08-21 15:06:41.966', 1818179516122071040, '2024-08-21 15:22:14.142', -1, 'M域知识助手', '3', '-1', '2', 'context', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1826438688639356928, '输出语句中，与安全相关的名词{{context}}', 1770708373253357568, '2024-08-22 09:58:11.642', 1770708373253357568, '2024-08-22 09:58:29.899', -1, '广东测试_zb', '3', '-1', '2', 'context', NULL, '441000000000000103054855');
INSERT INTO meta.mm_prompts VALUES (1826439914340814848, '作为一个云网运行安全专家，初级云网运行工程师给你提问，提问内容是：{{question}},请根据相关专业知识:{{kownledge}}回答，请不要回答与专业无关的问题。', 1807326216652914688, '2024-08-22 10:03:03.849', 1807326216652914688, '2024-08-22 10:03:03.849', -1, '第二组态势感知', '3', '-1', '2', 'question,kownledge', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1828984923216744448, '# 角色

你是一名AI故障处理专家，精通移动通信网络及相关技术。你的任务是根据用户的投诉内容提取关键信息，并基于具体场景提供有效的分析和解决方案，同时确保用户体验的流畅性和高效性。
', 1828258853725532160, '2024-08-29 10:36:01.37', 1828258853725532160, '2024-08-29 10:40:17.329', -1, '移动投诉处理角色', '', '-1', '2', '', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1864151254782803968, '你是一个助手，请回答{{}}', 1777501031577882624, '2024-12-04 11:34:28.054', 1777501031577882624, '2024-12-04 11:34:28.054', -1, '测试', '', '-1', '2', '', NULL, '621102000900800000004455');
INSERT INTO meta.mm_prompts VALUES (1864496098248065024, 'hello', 1863412004509716480, '2024-12-05 10:24:45.149', 1863412004509716480, '2024-12-05 10:25:16.214', -1, 'zhangchuantest', '', '-1', '2', '', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1864599189614067712, '作为一个知识助手，请列举{{system_name}}系统说明。', 1772104465751478272, '2024-12-05 17:14:24.045', 1772104465751478272, '2024-12-05 17:14:24.045', -1, 'test', '3', '-1', '2', 'system_name', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1828986196758433792, '# 角色

你是一名AI故障处理专家，精通移动通信网络及相关技术。你的任务是根据用户的投诉内容提取关键信息，并基于具体场景提供有效的分析和解决方案，同时确保用户体验的流畅性和高效性。

# Initialization

您好！请详细描述您的问题，并尽可能提供以下信息：问题发生的时间、频率、涉及的服务（如语音、短信、数据）、以及是否曾尝试过自行解决等。

# 输出格式

输出格式为JSON

```json
"start_time": "{根据用户投诉内容提取的故障开始时间，使用固定格式“yyyy-MM-dd HH24:mi:ss”。如果提供的信息不包含完整的日期或时间，则此字段留空。}",
  "end_time": "{根据用户投诉内容提取的故障结束时间，使用固定格式“yyyy-MM-dd HH24:mi:ss”。如果没有结束时间信息或提供的信息不完整，则此字段留空。}"{
  "scene": "{根据用户投诉内容提取并生成的标准化场景格式，例如“移动二级-省内-流量问题”。}",
  "phone": "{从用户投诉内容中提取的手机号码。如果无法提取号码，提示用户提供号码。}"', 1828258853725532160, '2024-08-29 10:41:04.884', 1828258853725532160, '2024-08-29 10:41:04.884', -1, 'Initialization', '', '-1', '2', '', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1828986265834426368, '### 使用限制：

1. 仅输出核心信息：AI响应中只应包含投诉场景、故障号码和故障描述三部分，严格避免输出其他不相关的内容。
2. 遵守格式：每一部分必须按照预定义的格式进行输出，确保响应的一致性和标准化。
3. 信息提取准确性：确保从用户投诉内容中准确提取关键信息，避免包含推测性或不相关的信息。
4. 不要输出其他内容

# 投诉场景生成规则：

1. 网络层级：
   - 根据用户提供的信息判断网络层级，例如“移动二级”、“移动一级”等。
2. 问题类型：
   - 根据用户的投诉内容识别问题类型，例如“漫出”、“漫入”、“省内”等。
3. 具体故障：
   - 结合用户描述与知识点，确定具体故障类型，例如“基础业务无法使用”、“应用无法访问”等,流量相关的问题需要区分通用流量和定向流量。
', 1828258853725532160, '2024-08-29 10:41:21.353', 1828258853725532160, '2024-08-29 10:41:21.353', -1, '使用限制', '', '-1', '2', '', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1831960103631261696, '你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确
用户提问：{{question}}
已知的信息：{{stem}}
输出格式说明:
问题:
回答:', 0, '2024-09-06 15:38:19.536', 0, '2024-09-06 15:43:50.852', -1, '0906prompt创建', '3', '-1', '2', 'question,stem', NULL, '71101000xxxx00000000');
INSERT INTO meta.mm_prompts VALUES (1833758961071886336, '你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确
用户提问：{{question}}
已知的信息：{{stem}}
输出格式说明:
问题:
回答:
请注意问题仅需复述用户提问即可', 1769975988296400896, '2024-09-11 14:46:20.592', 1769975988296400896, '2024-09-11 14:46:20.592', -1, '0911_一问一答模板', '3', '-1', '2', 'question,stem', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1833789112597942272, '问题: 你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确。用户提问：{question}。已知的信息：{stem}。\n回答: {answer}', 1769975988296400896, '2024-09-11 16:46:09.255', 1769975988296400896, '2024-09-11 16:46:09.255', -1, '优化模板', '', '-1', '2', '', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1834395546474348544, '你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确
用户提问：{{question}}
已知的信息：{{stem}}
输出格式说明:
问题:
回答:
请注意问题仅需复述用户提问即可', 1769975988296400896, '2024-09-13 08:55:54.41', 1769975988296400896, '2024-09-13 08:55:54.41', -1, '913演示', '3', '-1', '2', 'question,stem', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1836664886762504192, '作为一个气象专家，请帮我查询{{city}}的天气。', 1762769712366129152, '2024-09-19 15:13:27.266', 1762769712366129152, '2024-09-19 15:13:27.266', -1, '气象助手', '3', '-1', '2', 'city', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1834430507256479744, '作为一个装维助手，请输入在装维过程中，遇到{{变量}}如何解决？', 1772163789678891008, '2024-09-13 11:14:49.658', 1772163789678891008, '2024-09-13 11:14:49.658', -1, 'test_01', '3', '-1', '2', '变量', NULL, '631100000000000067126468');
INSERT INTO meta.mm_prompts VALUES (1834432768191856640, '你是一个AI助手，你的这任务是解决用户的问题。

用户的问题是：
{{query}}', 1767801007718318080, '2024-09-13 11:23:48.706', 1767801007718318080, '2024-09-13 11:23:48.706', -1, '我的测试模板', '3', '-1', '2', 'query', NULL, '521000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1834437791277744128, '作为一个核心网助手，你可以问我，核心网的主要功能是什么', 1810504256261947392, '2024-09-13 11:43:46.303', 1810504256261947392, '2024-09-13 11:43:46.303', -1, '核心网问答', '', '-1', '2', '', NULL, '631100000000000067126468');
INSERT INTO meta.mm_prompts VALUES (1834478190893563904, '请整理公司{{type}}相关的装维知识，请总结并输出关联的内容，不要输出无关内容。', 1769635374048145408, '2024-09-13 14:24:18.323', 1769635374048145408, '2024-09-13 14:24:18.323', -1, '茹妞妞的prompt模板', '3', '-1', '2', 'type', NULL, '141011000000000000049839');
INSERT INTO meta.mm_prompts VALUES (1837086802807717888, '请整理中国电信黑龙江分公司{{type}}相关的规章制度，对应制度条例可能包括{{content}}，请总结并输出关联的内容，不要输出无关内容。', 1671420655862226944, '2024-09-20 19:09:59.889', 1671420655862226944, '2024-09-20 19:14:32.108', -1, '规章制度知识助手自定义模板', '3', '-1', '2', 'type,content', NULL, '231009100000000000000001');
INSERT INTO meta.mm_prompts VALUES (1837304425390637056, '请整理公司{{type}}相关的规章制度，对应制度条例可能包括{{content}}，请总结并输出关联的内容，不要输出无关内容。', 1671420655862226944, '2024-09-21 09:34:45.136', 1671420655862226944, '2024-09-21 09:34:45.136', -1, '演示模板', '3', '-1', '2', 'type,content', NULL, '231009100000000000000001');
INSERT INTO meta.mm_prompts VALUES (1838133101753630720, '作为话术质检专家，请根据装维人员说话内容"{{content}}"，判断是否满足宽带已装通或修复、完成宽带测速宣贯、完成10分好评宣贯的质检要求；每个质检要求得5分，请计算本次质检得分。', 1706954484721459200, '2024-09-23 16:27:36.985', 1706954484721459200, '2024-09-23 16:43:25.562', -1, '话术质检专家意图识别', '3', '-1', '2', 'content', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1839211886146060288, '以上内容生成问答对，按问题和答案生成表格', 1706954484721459200, '2024-09-26 15:54:19.225', 1706954484721459200, '2024-09-26 15:54:19.225', -1, '问答对生成助手', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1844296168304640000, '你是集团变更操作专家，我们希望您能对方案{{scheme}}出现的问题进行详细且全面的回答： {{request}}', 1838389321360576512, '2024-10-10 16:37:26.57', 1838389321360576512, '2024-10-10 16:37:26.57', -1, '方案模板_测试', '3', '-1', '2', 'scheme,request', NULL, '351000000000000000000001');
INSERT INTO meta.mm_prompts VALUES (1846010083862478848, '你是安徽网优中心员工，请从与{{anli}}相关的案例中选择答案', 1675673728600440832, '2024-10-15 10:07:55.864', 1675673728600440832, '2024-10-15 10:07:55.864', -1, 'yzy测试', '3', '-1', '2', 'anli', NULL, '341033000000000002392001');
INSERT INTO meta.mm_prompts VALUES (1847118550954246144, '1018drr测试', 1817840893816274944, '2024-10-18 11:32:35.028', 1817840893816274944, '2024-10-18 11:32:35.028', -1, '1018drr测试', '', '-1', '2', '', NULL, '151000000000000268503725');
INSERT INTO meta.mm_prompts VALUES (1848642462015913984, 'test_demo_pompt', 1848547897886478336, '2024-10-22 16:28:03.723', 1848547897886478336, '2024-10-22 16:28:03.723', -1, 'my_prompt', '', '-1', '2', '', NULL, '531000000000000000000477');
INSERT INTO meta.mm_prompts VALUES (1862016702571679744, '你是应知应会小助手，现在接收到的易问工单内容为：{{content}}，请根据你已有经验分析该工单内容，并归纳所属团队。', 1843955104632070144, '2024-11-28 14:12:31.147', 1843955104632070144, '2024-11-28 14:12:31.147', -1, '易问工单分单', '3', '-1', '2', 'content', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1864130089989275648, '做一下自我介绍', 1807944365087571968, '2024-12-04 10:10:21.975', 1807944365087571968, '2024-12-04 10:10:21.975', -1, '测试', '', '-1', '2', '', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1864941585867894784, '1', 1863412004509716480, '2024-12-06 15:54:57.687', 1863412004509716480, '2024-12-06 15:54:57.687', -1, 'moban1', '', '-1', '2', '', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1866039341554495488, '角色:{你是一个电信行业的宽带客服专家，拥有丰富的行业经验。}
背景描述：{输入的内容是智能AI客服和用户之间的对话。}

任务描述：{你需要从对话内容分析出客户最新一轮对话的真实意图。}

谨记：{
分析步骤：{}
主题1：{}
主题2：{}
正确的输出例子
    例1:
        用户输入:
                用户：我家宽带不能上网
                AI：亲亲，非常抱歉给您带来不好的体验了。
                AI：亲亲，请问您家的哪条宽带有问题呢?
                    宽带xxxx1
                    安徽合肥合肥市蜀山区笔架山街道圣泉路。
                    宽带xxxx2
                    安徽合肥市庐阳区杏林路
                用户：杏林路
                    模型输出:
                {  }
}
          ', 1696334365593219072, '2024-12-09 16:37:03.028', 1696334365593219072, '2024-12-09 16:37:03.028', -1, '意图识别测试', '', '-1', '2', '', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1867104020225748992, '当然可以，下面是一个简短的故事，名为《时间的馈赠》。请注意，故事超过1000字，以适应您的需求。在遥远的星系中，有一个叫做艾尔维亚的星球，它被誉为时间的宝石，因为那里的居民拥有操控时间的能力。艾尔维亚星球上，有一位名叫伊尔文的少年，他是时间学者的儿子，对时间的奥秘充满了好奇。伊尔文的父亲，艾尔文教授，是一位研究时间流动性的学者，他总是教导伊尔文，时间就像一条永不停息的河流，每个人都在其中流淌。然而，教授从未真正解释过时间的边界和极限，这使伊尔文心中充满了疑惑。一天，一个神秘的访客来到艾尔文的家，他自称是时间守护者，名叫诺曼。诺曼告诉伊尔文，他的父亲因为探索时间的终极秘密，触碰到了一个禁忌领域，被时间的力量困在了过去。为了拯救父亲，伊尔文必须接受诺曼的挑战，通过时间旅行的考验。诺曼赠给伊尔文一个古老的时钟，这是他的父亲留下的遗物，拥有启动时间旅行的力量。伊尔文带着时钟，踏上了寻找父亲的旅程。他穿越了过去和未来，见证了时间的流转和生命的轮回。在旅途中，伊尔文遇见了各种各样的人，他们都在时间的河流中寻找自己的故事。他遇到了一个勇敢的战士，他在时间的战争中坚守正义；一个温柔的画家，她用时间的色', 1807944365087571968, '2024-12-12 15:07:42.208', 1807944365087571968, '2024-12-12 15:11:42.859', -1, '创建自定义模版', '', '-1', '2', '', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1871809527488942080, '<专业类别>[najor]<省警美型>[alarn)<告警位置>[site}
<故障描述>[fault text]
我希望你扮演一个网络运维专家，请结合上述表格信息回答用户的问题，你的目标是对告警进行初次定位，通常的步骤如下:1,明确用户问题的告警类型，从上述表格进行模制查询，找到初次定位1级、初次定位2级和推荐的解决方案。2.如果衣格不为空将查询结果综合地反调给用户，如果表格査湖结果为空，请结合你自己学习的知识进行作答。如果菜一项为空，也请结合你自己学习的知识进行作答3.请严格按照我给你的格式进行作答:初次定位1级为xxx；初次定位2级为xX；推荐的解决方案为xxX''
', 1831140792429654016, '2024-12-25 14:45:42.585', 1831140792429654016, '2024-12-25 14:45:42.585', -1, '北京', '', '-1', '2', '', NULL, '111000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1873580140364795904, '请列举出公司{{type}}相关的规章制度，包括但不限于但不限于：操作规程、应急预案、培训、设备维护保养、检查等。', 1833391222272987136, '2024-12-30 12:01:29.569', 1833391222272987136, '2024-12-30 12:01:29.569', -1, 'test', '3', '-1', '2', 'type', NULL, '501102000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1876519608314990592, '作为{{province}}综维客服，你能根据用户问题获取本省综维领域知识，并结合所获取知识整理内容为用户提供满意的回复。当前用户问题为{{question}}，所获得的知识为{{knowledge}}，请准确回答客户问题，对应提解释说明。', 1732234610815643648, '2025-01-07 14:41:53.346', 1732234610815643648, '2025-01-07 14:41:53.346', -1, 'test提示工程', '3', '-1', '2', 'province,question,knowledge', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1876520107705602048, '为了更准确地回答您的问题，我需要具体的{{province}}和{{question}}。请提供相关信息，以便我能够获取相关的综维领域知识并为您提供满意的回复。在收到您的详细问题后，我将根据所获得的{{knowledge}}为您整理内容并给出详细回答。', 1732234610815643648, '2025-01-07 14:43:52.396', 1732234610815643648, '2025-01-07 14:43:52.396', -1, '保存的提示工程', '3', '-1', '2', 'province,question,knowledge', NULL, '911010000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1876819175619137536, '{}', 1778436709531303936, '2025-01-08 10:32:15.74', 1778436709531303936, '2025-01-08 10:32:15.74', -1, '测试20250108', '', '-1', '2', '', NULL, '500000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1880066290732380160, '作为一个资源助手，请列举所有设备名称', 1787660760191393792, '2025-01-17 09:35:08.33', 1787660760191393792, '2025-01-17 09:35:08.33', -1, '测试模板', '', '-1', '2', '', NULL, '500000000000000000000000');
INSERT INTO meta.mm_prompts VALUES (1882364702425231360, '随机提供装维知识在线考试题目，对作答进行评分', 1775387523547578368, '2025-01-23 17:48:12.367', 1775387523547578368, '2025-01-23 17:48:12.367', -1, '装维知识在线考试', '', '-1', '2', '', NULL, '441000000000000103054855');


--
-- TOC entry 3863 (class 0 OID 306869)
-- Dependencies: 258
-- Data for Name: mm_province; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_province VALUES (1846376201518952448, '上海市', '311115000000000000000000', 'SH');
INSERT INTO meta.mm_province VALUES (1846376201523146752, '云南省', '531000000000000000000477', 'YN');
INSERT INTO meta.mm_province VALUES (1846376201523146753, '内蒙古自治区', '151000000000000268503725', 'NM');
INSERT INTO meta.mm_province VALUES (1846376201523146754, '北京市', '111000000000000000000000', 'BJ');
INSERT INTO meta.mm_province VALUES (1846376201523146755, '吉林省', '221000100000000000000001', 'JL');
INSERT INTO meta.mm_province VALUES (1846376201523146756, '四川省', '511000000000000000000000', 'SC');
INSERT INTO meta.mm_province VALUES (1846376201523146757, '天津市', '121000000000000000000000', 'TJ');
INSERT INTO meta.mm_province VALUES (1846376201523146758, '宁夏回族自治区', '641102000000000000000069', 'NX');
INSERT INTO meta.mm_province VALUES (1846376201523146759, '安徽省', '341033000000000002392001', 'AH');
INSERT INTO meta.mm_province VALUES (1846376201523146760, '山东省', '371110000000000000000021', 'SD');
INSERT INTO meta.mm_province VALUES (1846376201523146761, '山西省', '141011000000000000049839', 'SX');
INSERT INTO meta.mm_province VALUES (1846376201523146762, '广东省', '441000000000000103054855', 'GD');
INSERT INTO meta.mm_province VALUES (1846376201523146763, '广西壮族自治区', '451102000000000021333149', 'GX');
INSERT INTO meta.mm_province VALUES (1846376201523146764, '新疆维吾尔自治区', '651102000000000021347615', 'XJ');
INSERT INTO meta.mm_province VALUES (1846376201523146765, '江苏省', '321122930000000000000014', 'JS');
INSERT INTO meta.mm_province VALUES (1846376201523146766, '江西省', '361011000000000036696304', 'JX');
INSERT INTO meta.mm_province VALUES (1846376201523146767, '河北省', '131000000000000001216374', 'HE');
INSERT INTO meta.mm_province VALUES (1846376201523146768, '河南省', '411000000000000001950388', 'HA');
INSERT INTO meta.mm_province VALUES (1846376201523146769, '浙江省', '331711000000000021720883', 'ZJ');
INSERT INTO meta.mm_province VALUES (1846376201523146770, '海南省', '461000000000000000026357', 'HI');
INSERT INTO meta.mm_province VALUES (1846376201523146771, '湖北省', '421100000000000000064915', 'HB');
INSERT INTO meta.mm_province VALUES (1846376201523146772, '湖南省', '431102000000000000000001', 'HN');
INSERT INTO meta.mm_province VALUES (1846376201523146773, '甘肃省', '621102000900800000004455', 'GS');
INSERT INTO meta.mm_province VALUES (1846376201523146774, '福建省', '351000000000000000000001', 'FJ');
INSERT INTO meta.mm_province VALUES (1846376201523146775, '西藏自治区', '541000000000000261037094', 'XZ');
INSERT INTO meta.mm_province VALUES (1846376201523146776, '贵州省', '521000000000000000000001', 'GZ');
INSERT INTO meta.mm_province VALUES (1846376201523146777, '辽宁省', '211000000000000000000001', 'LN');
INSERT INTO meta.mm_province VALUES (1846376201523146778, '重庆市', '501102000000000000000000', 'CQ');
INSERT INTO meta.mm_province VALUES (1846376201523146779, '陕西省', '611000000000001824854982', 'SN');
INSERT INTO meta.mm_province VALUES (1846376201523146780, '集团', '911010000000000000000000', 'HQ');
INSERT INTO meta.mm_province VALUES (1846376201523146781, '青海省', '631100000000000067126468', 'QH');
INSERT INTO meta.mm_province VALUES (1846376201523146782, '黑龙江省', '231009100000000000000001', 'HL');


--
-- TOC entry 3864 (class 0 OID 306872)
-- Dependencies: 259
-- Data for Name: mm_user; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_user VALUES (1890280795059261440, '1234', 'admin', 'admin', '911010000000000000000000', '集团', '["ALL"]', '["ALL"]', '17312345678', '', 'xxx研发部', 'xxx公司', 0, 0, '2025-02-14 14:19:37', 0, '2025-02-17 09:57:39', '2025-02-17 09:57:39', '1', '0', '0', '0', 'xxx公司总部', 'HQ');


--
-- TOC entry 3865 (class 0 OID 306882)
-- Dependencies: 260
-- Data for Name: mm_user_model; Type: TABLE DATA; Schema: meta; Owner: -
--

INSERT INTO meta.mm_user_model VALUES (1890299278195577240, 1890280795059261440, 1790249036662829056, 1890239024003395584, '2025-02-14 15:17:22.941', 1890239024003395584, '2025-02-14 15:17:22.941', '1');
INSERT INTO meta.mm_user_model VALUES (1890299278208159778, 1890280795059261440, 1790249036662829056, 1890239024003395584, '2025-02-14 15:17:23.145', 1890239024003395584, '2025-02-14 15:17:23.145', '2');
INSERT INTO meta.mm_user_model VALUES (1890299289335648276, 1890280795059261440, 1807586796071366656, 1890239024003395584, '2025-02-14 15:17:25.439', 1890239024003395584, '2025-02-14 15:17:25.439', '1');
INSERT INTO meta.mm_user_model VALUES (1890299289335649417, 1890280795059261440, 1807586796071366656, 1890239024003395584, '2025-02-14 15:17:25.649', 1890239024003395584, '2025-02-14 15:17:25.649', '2');
INSERT INTO meta.mm_user_model VALUES (1890299298294682185, 1890280795059261440, 1807586796075560960, 1890239024003395584, '2025-02-14 15:17:27.62', 1890239024003395584, '2025-02-14 15:17:27.62', '1');
INSERT INTO meta.mm_user_model VALUES (1890299298298876460, 1890280795059261440, 1807586796075560960, 1890239024003395584, '2025-02-14 15:17:27.772', 1890239024003395584, '2025-02-14 15:17:27.772', '2');
INSERT INTO meta.mm_user_model VALUES (1890299307333407855, 1890280795059261440, 1807586796075560961, 1890239024003395584, '2025-02-14 15:17:29.741', 1890239024003395584, '2025-02-14 15:17:29.741', '1');
INSERT INTO meta.mm_user_model VALUES (1890299307337601701, 1890280795059261440, 1807586796075560961, 1890239024003395584, '2025-02-14 15:17:29.925', 1890239024003395584, '2025-02-14 15:17:29.925', '2');
INSERT INTO meta.mm_user_model VALUES (1890299315646517534, 1890280795059261440, 1823985532368408579, 1890239024003395584, '2025-02-14 15:17:31.672', 1890239024003395584, '2025-02-14 15:17:31.672', '1');
INSERT INTO meta.mm_user_model VALUES (1890299315646518675, 1890280795059261440, 1823985532368408579, 1890239024003395584, '2025-02-14 15:17:31.831', 1890239024003395584, '2025-02-14 15:17:31.831', '2');
INSERT INTO meta.mm_user_model VALUES (1890299325784150283, 1890280795059261440, 1823985532368408580, 1890239024003395584, '2025-02-14 15:17:34.086', 1890239024003395584, '2025-02-14 15:17:34.086', '1');
INSERT INTO meta.mm_user_model VALUES (1890299325784151424, 1890280795059261440, 1823985532368408580, 1890239024003395584, '2025-02-14 15:17:34.184', 1890239024003395584, '2025-02-14 15:17:34.184', '2');
INSERT INTO meta.mm_user_model VALUES (1933111438979051906, 1890280795059261440, 1823985532368408582, 1890280795059261440, '2025-06-12 18:37:37.302', 1890280795059261440, '2025-06-12 18:37:37.302', '1');


--
-- TOC entry 3867 (class 0 OID 306888)
-- Dependencies: 262
-- Data for Name: mm_dcoos_api; Type: TABLE DATA; Schema: register; Owner: -
--



--
-- TOC entry 3868 (class 0 OID 306897)
-- Dependencies: 263
-- Data for Name: mm_dcoos_api_template; Type: TABLE DATA; Schema: register; Owner: -
--



--
-- TOC entry 3869 (class 0 OID 306905)
-- Dependencies: 264
-- Data for Name: mm_area; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3870 (class 0 OID 306908)
-- Dependencies: 265
-- Data for Name: mm_deploy_area; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3871 (class 0 OID 306914)
-- Dependencies: 266
-- Data for Name: mm_deploy_server; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3872 (class 0 OID 306922)
-- Dependencies: 267
-- Data for Name: mm_deploy_server_card; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3873 (class 0 OID 306930)
-- Dependencies: 268
-- Data for Name: mm_deploy_task; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3884 (class 0 OID 371339)
-- Dependencies: 279
-- Data for Name: mm_task_group; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3883 (class 0 OID 327416)
-- Dependencies: 278
-- Data for Name: mm_train_dataset_join; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3874 (class 0 OID 306946)
-- Dependencies: 269
-- Data for Name: mm_train_task; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3875 (class 0 OID 306954)
-- Dependencies: 270
-- Data for Name: mm_train_task_demo; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 3876 (class 0 OID 306962)
-- Dependencies: 271
-- Data for Name: mm_train_task_eval; Type: TABLE DATA; Schema: train; Owner: -
--



--
-- TOC entry 4608 (class 0 OID 0)
-- Dependencies: 227
-- Name: mm_model_monitor_intf_id_seq; Type: SEQUENCE SET; Schema: log; Owner: -
--

SELECT pg_catalog.setval('log.mm_model_monitor_intf_id_seq', 10, true);


--
-- TOC entry 4609 (class 0 OID 0)
-- Dependencies: 229
-- Name: mm_model_monitor_model_id_seq; Type: SEQUENCE SET; Schema: log; Owner: -
--

SELECT pg_catalog.setval('log.mm_model_monitor_model_id_seq', 10, true);


--
-- TOC entry 4610 (class 0 OID 0)
-- Dependencies: 231
-- Name: mm_model_monitor_statistics_id_seq; Type: SEQUENCE SET; Schema: log; Owner: -
--

SELECT pg_catalog.setval('log.mm_model_monitor_statistics_id_seq', 2, true);


--
-- TOC entry 4611 (class 0 OID 0)
-- Dependencies: 272
-- Name: session_cache_id_seq; Type: SEQUENCE SET; Schema: log; Owner: -
--

SELECT pg_catalog.setval('log.session_cache_id_seq', 1, false);


--
-- TOC entry 4612 (class 0 OID 0)
-- Dependencies: 274
-- Name: mm_project_space_project_id_seq; Type: SEQUENCE SET; Schema: meta; Owner: -
--

SELECT pg_catalog.setval('meta.mm_project_space_project_id_seq', 1, false);


--
-- TOC entry 4613 (class 0 OID 0)
-- Dependencies: 261
-- Name: mm_api_id_seq; Type: SEQUENCE SET; Schema: register; Owner: -
--

SELECT pg_catalog.setval('register.mm_api_id_seq', 1, false);


--
-- TOC entry 3537 (class 2606 OID 315619)
-- Name: mm_job mm_job_pk; Type: CONSTRAINT; Schema: job; Owner: -
--

ALTER TABLE ONLY job.mm_job
    ADD CONSTRAINT mm_job_pk PRIMARY KEY (id);


--
-- TOC entry 3539 (class 2606 OID 315621)
-- Name: mm_job_log sys_job_log_pkey; Type: CONSTRAINT; Schema: job; Owner: -
--

ALTER TABLE ONLY job.mm_job_log
    ADD CONSTRAINT sys_job_log_pkey PRIMARY KEY (id);


--
-- TOC entry 3541 (class 2606 OID 315623)
-- Name: conversation_history conversation_history_pk; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.conversation_history
    ADD CONSTRAINT conversation_history_pk PRIMARY KEY (id);


--
-- TOC entry 3545 (class 2606 OID 315625)
-- Name: mm_log mm_log_pk; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_log
    ADD CONSTRAINT mm_log_pk PRIMARY KEY (id);


--
-- TOC entry 3547 (class 2606 OID 315627)
-- Name: mm_model_chat_log mm_model_chat_log_pk; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_chat_log
    ADD CONSTRAINT mm_model_chat_log_pk PRIMARY KEY (id);


--
-- TOC entry 3553 (class 2606 OID 315629)
-- Name: mm_model_monitor_intf mm_model_monitor_intf_pkey; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_intf
    ADD CONSTRAINT mm_model_monitor_intf_pkey PRIMARY KEY (id);


--
-- TOC entry 3558 (class 2606 OID 315631)
-- Name: mm_model_monitor_model mm_model_monitor_model_pkey; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_model
    ADD CONSTRAINT mm_model_monitor_model_pkey PRIMARY KEY (id);


--
-- TOC entry 3561 (class 2606 OID 315633)
-- Name: mm_model_monitor_statistics mm_model_monitor_statistics_pkey; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_model_monitor_statistics
    ADD CONSTRAINT mm_model_monitor_statistics_pkey PRIMARY KEY (id);


--
-- TOC entry 3563 (class 2606 OID 315635)
-- Name: mm_user_login_log mm_user_visit_pk; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_user_login_log
    ADD CONSTRAINT mm_user_visit_pk PRIMARY KEY (id);


--
-- TOC entry 3565 (class 2606 OID 315637)
-- Name: mm_user_login_log mm_user_visit_unique; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.mm_user_login_log
    ADD CONSTRAINT mm_user_visit_unique UNIQUE (user_id, login_date);


--
-- TOC entry 3663 (class 2606 OID 327388)
-- Name: session_cache session_cache_pkey; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.session_cache
    ADD CONSTRAINT session_cache_pkey PRIMARY KEY (id);


--
-- TOC entry 3665 (class 2606 OID 327390)
-- Name: session_cache session_cache_province_session_id_intention_key; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.session_cache
    ADD CONSTRAINT session_cache_province_session_id_intention_key UNIQUE (province, session_id, intention);


--
-- TOC entry 3573 (class 2606 OID 315639)
-- Name: mm_cluster_metric mm_cluster_metric_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_cluster_metric
    ADD CONSTRAINT mm_cluster_metric_pkey PRIMARY KEY (id);


--
-- TOC entry 3575 (class 2606 OID 315641)
-- Name: mm_cluster_metric mm_cluster_metric_unidx; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_cluster_metric
    ADD CONSTRAINT mm_cluster_metric_unidx UNIQUE (cluster_code, category, code);


--
-- TOC entry 3569 (class 2606 OID 315643)
-- Name: mm_cluster mm_cluster_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_cluster
    ADD CONSTRAINT mm_cluster_pkey PRIMARY KEY (id);


--
-- TOC entry 3571 (class 2606 OID 315645)
-- Name: mm_cluster mm_cluster_uidx; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_cluster
    ADD CONSTRAINT mm_cluster_uidx UNIQUE (code);


--
-- TOC entry 3579 (class 2606 OID 315647)
-- Name: mm_data_set_file mm_data_set_file_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_data_set_file
    ADD CONSTRAINT mm_data_set_file_pkey PRIMARY KEY (id);


--
-- TOC entry 3577 (class 2606 OID 315649)
-- Name: mm_data_set mm_data_set_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_data_set
    ADD CONSTRAINT mm_data_set_pkey PRIMARY KEY (id);


--
-- TOC entry 3584 (class 2606 OID 315651)
-- Name: mm_dict_data mm_dict_data_unique; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_dict_data
    ADD CONSTRAINT mm_dict_data_unique UNIQUE (dict_code, dict_value);


--
-- TOC entry 3588 (class 2606 OID 315653)
-- Name: mm_dict_type mm_dict_type_dict_type_key; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_dict_type
    ADD CONSTRAINT mm_dict_type_dict_type_key UNIQUE (dict_type);


--
-- TOC entry 3594 (class 2606 OID 315655)
-- Name: mm_group mm_group_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_group
    ADD CONSTRAINT mm_group_pkey PRIMARY KEY (id);


--
-- TOC entry 3596 (class 2606 OID 315657)
-- Name: mm_group_user mm_group_user_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_group_user
    ADD CONSTRAINT mm_group_user_pkey PRIMARY KEY (id);


--
-- TOC entry 3682 (class 2606 OID 379573)
-- Name: mm_host mm_host_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_host
    ADD CONSTRAINT mm_host_pk PRIMARY KEY (id);


--
-- TOC entry 3609 (class 2606 OID 315659)
-- Name: mm_order_node mm_order_node_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_order_node
    ADD CONSTRAINT mm_order_node_pk PRIMARY KEY (id);


--
-- TOC entry 3605 (class 2606 OID 315661)
-- Name: mm_order mm_order_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_order
    ADD CONSTRAINT mm_order_pk PRIMARY KEY (id);


--
-- TOC entry 3613 (class 2606 OID 315663)
-- Name: mm_order_user mm_order_user_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_order_user
    ADD CONSTRAINT mm_order_user_pk PRIMARY KEY (id);


--
-- TOC entry 3680 (class 2606 OID 379566)
-- Name: mm_pod_detail mm_pod_detail_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_pod_detail
    ADD CONSTRAINT mm_pod_detail_pk PRIMARY KEY (id);


--
-- TOC entry 3617 (class 2606 OID 315665)
-- Name: mm_pr_test_set_evaluation_detail mm_pr_test_set_evaluation_detail_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_pr_test_set_evaluation_detail
    ADD CONSTRAINT mm_pr_test_set_evaluation_detail_pk PRIMARY KEY (id);


--
-- TOC entry 3615 (class 2606 OID 315667)
-- Name: mm_pr_test_set_evaluation mm_pr_test_set_evaluation_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_pr_test_set_evaluation
    ADD CONSTRAINT mm_pr_test_set_evaluation_pk PRIMARY KEY (id);


--
-- TOC entry 3619 (class 2606 OID 315669)
-- Name: mm_pr_test_set_evaluation_score mm_pr_test_set_evaluation_score_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_pr_test_set_evaluation_score
    ADD CONSTRAINT mm_pr_test_set_evaluation_score_pkey PRIMARY KEY (id);


--
-- TOC entry 3678 (class 2606 OID 379552)
-- Name: mm_project_host_rel mm_project_host_rel_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_project_host_rel
    ADD CONSTRAINT mm_project_host_rel_pk PRIMARY KEY (id);


--
-- TOC entry 3621 (class 2606 OID 315671)
-- Name: mm_prompt_category_detail mm_prompt_category_detail_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompt_category_detail
    ADD CONSTRAINT mm_prompt_category_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 3623 (class 2606 OID 315673)
-- Name: mm_prompt_resp mm_prompt_resp_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompt_resp
    ADD CONSTRAINT mm_prompt_resp_pkey PRIMARY KEY (id);


--
-- TOC entry 3625 (class 2606 OID 315675)
-- Name: mm_prompt_resp mm_prompt_resp_un; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompt_resp
    ADD CONSTRAINT mm_prompt_resp_un UNIQUE (data_set_id);


--
-- TOC entry 3627 (class 2606 OID 315677)
-- Name: mm_prompt_response_detail mm_prompt_response_detail_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompt_response_detail
    ADD CONSTRAINT mm_prompt_response_detail_pkey PRIMARY KEY (id);


--
-- TOC entry 3633 (class 2606 OID 315679)
-- Name: mm_province mm_province_pk; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_province
    ADD CONSTRAINT mm_province_pk PRIMARY KEY (id);


--
-- TOC entry 3639 (class 2606 OID 315681)
-- Name: mm_user_model mm_user_base_model_pkey; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_user_model
    ADD CONSTRAINT mm_user_base_model_pkey PRIMARY KEY (id);


--
-- TOC entry 3567 (class 2606 OID 315683)
-- Name: mm_application_square pk_mm_application_square; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_application_square
    ADD CONSTRAINT pk_mm_application_square PRIMARY KEY (id);


--
-- TOC entry 3586 (class 2606 OID 315685)
-- Name: mm_dict_data pk_mm_dict_data; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_dict_data
    ADD CONSTRAINT pk_mm_dict_data PRIMARY KEY (dict_code);


--
-- TOC entry 3590 (class 2606 OID 315687)
-- Name: mm_dict_type pk_mm_dict_type; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_dict_type
    ADD CONSTRAINT pk_mm_dict_type PRIMARY KEY (dict_id);


--
-- TOC entry 3592 (class 2606 OID 315689)
-- Name: mm_domain pk_mm_domain; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_domain
    ADD CONSTRAINT pk_mm_domain PRIMARY KEY (id);


--
-- TOC entry 3598 (class 2606 OID 315691)
-- Name: mm_model pk_mm_model; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_model
    ADD CONSTRAINT pk_mm_model PRIMARY KEY (id);


--
-- TOC entry 3602 (class 2606 OID 315693)
-- Name: mm_model_train_param pk_mm_model_param; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_model_train_param
    ADD CONSTRAINT pk_mm_model_param PRIMARY KEY (id);


--
-- TOC entry 3600 (class 2606 OID 315695)
-- Name: mm_model_train pk_mm_model_train; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_model_train
    ADD CONSTRAINT pk_mm_model_train PRIMARY KEY (id);


--
-- TOC entry 3667 (class 2606 OID 327401)
-- Name: mm_project_space pk_mm_project_space; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_project_space
    ADD CONSTRAINT pk_mm_project_space PRIMARY KEY (project_id);


--
-- TOC entry 3669 (class 2606 OID 327408)
-- Name: mm_project_user pk_mm_project_user; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_project_user
    ADD CONSTRAINT pk_mm_project_user PRIMARY KEY (project_id, user_id);


--
-- TOC entry 3671 (class 2606 OID 327415)
-- Name: mm_project_user_role pk_mm_project_user_role; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_project_user_role
    ADD CONSTRAINT pk_mm_project_user_role PRIMARY KEY (project_id, user_id);


--
-- TOC entry 3629 (class 2606 OID 315697)
-- Name: mm_prompt_templates pk_mm_prompt_templates; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompt_templates
    ADD CONSTRAINT pk_mm_prompt_templates PRIMARY KEY (id);


--
-- TOC entry 3631 (class 2606 OID 315699)
-- Name: mm_prompts pk_mm_prompts; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_prompts
    ADD CONSTRAINT pk_mm_prompts PRIMARY KEY (id);


--
-- TOC entry 3635 (class 2606 OID 315701)
-- Name: mm_user pk_mm_user; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_user
    ADD CONSTRAINT pk_mm_user PRIMARY KEY (id);


--
-- TOC entry 3637 (class 2606 OID 315703)
-- Name: mm_user uidex_mm_user_employee_num; Type: CONSTRAINT; Schema: meta; Owner: -
--

ALTER TABLE ONLY meta.mm_user
    ADD CONSTRAINT uidex_mm_user_employee_num UNIQUE (employee_number);


--
-- TOC entry 3641 (class 2606 OID 315705)
-- Name: mm_dcoos_api mm_api_pk; Type: CONSTRAINT; Schema: register; Owner: -
--

ALTER TABLE ONLY register.mm_dcoos_api
    ADD CONSTRAINT mm_api_pk PRIMARY KEY (id);


--
-- TOC entry 3643 (class 2606 OID 315707)
-- Name: mm_dcoos_api_template mm_dcoos_api_template_pk; Type: CONSTRAINT; Schema: register; Owner: -
--

ALTER TABLE ONLY register.mm_dcoos_api_template
    ADD CONSTRAINT mm_dcoos_api_template_pk PRIMARY KEY (code);


--
-- TOC entry 3645 (class 2606 OID 315709)
-- Name: mm_area mm_area_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_area
    ADD CONSTRAINT mm_area_pkey PRIMARY KEY (id);


--
-- TOC entry 3647 (class 2606 OID 315711)
-- Name: mm_deploy_area mm_deploy_area_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_deploy_area
    ADD CONSTRAINT mm_deploy_area_pkey PRIMARY KEY (id);


--
-- TOC entry 3651 (class 2606 OID 315713)
-- Name: mm_deploy_server_card mm_deploy_server_card_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_deploy_server_card
    ADD CONSTRAINT mm_deploy_server_card_pkey PRIMARY KEY (id);


--
-- TOC entry 3649 (class 2606 OID 315715)
-- Name: mm_deploy_server mm_deploy_server_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_deploy_server
    ADD CONSTRAINT mm_deploy_server_pkey PRIMARY KEY (id);


--
-- TOC entry 3653 (class 2606 OID 315717)
-- Name: mm_deploy_task mm_deploy_task_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_deploy_task
    ADD CONSTRAINT mm_deploy_task_pkey PRIMARY KEY (id);


--
-- TOC entry 3676 (class 2606 OID 371345)
-- Name: mm_task_group mm_task_group_pk; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_task_group
    ADD CONSTRAINT mm_task_group_pk PRIMARY KEY (id);


--
-- TOC entry 3673 (class 2606 OID 327420)
-- Name: mm_train_dataset_join mm_train_dataset_join_pk; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_train_dataset_join
    ADD CONSTRAINT mm_train_dataset_join_pk PRIMARY KEY (id);


--
-- TOC entry 3659 (class 2606 OID 315719)
-- Name: mm_train_task_eval mm_train_task_eval_pkey; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_train_task_eval
    ADD CONSTRAINT mm_train_task_eval_pkey PRIMARY KEY (id);


--
-- TOC entry 3655 (class 2606 OID 315721)
-- Name: mm_train_task pk_mm_train_task; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_train_task
    ADD CONSTRAINT pk_mm_train_task PRIMARY KEY (id);


--
-- TOC entry 3657 (class 2606 OID 315723)
-- Name: mm_train_task_demo pk_mm_train_task_demo; Type: CONSTRAINT; Schema: train; Owner: -
--

ALTER TABLE ONLY train.mm_train_task_demo
    ADD CONSTRAINT pk_mm_train_task_demo PRIMARY KEY (id);


--
-- TOC entry 3542 (class 1259 OID 315724)
-- Name: conversation_history_time_idx; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX conversation_history_time_idx ON log.conversation_history USING btree ("time");


--
-- TOC entry 3543 (class 1259 OID 315730)
-- Name: conversation_history_user_id_idx; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX conversation_history_user_id_idx ON log.conversation_history USING btree (user_id, session_id);


--
-- TOC entry 3660 (class 1259 OID 327391)
-- Name: idx_session_intention_province; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX idx_session_intention_province ON log.session_cache USING btree (session_id, intention, province);


--
-- TOC entry 3661 (class 1259 OID 327392)
-- Name: idx_update_time; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX idx_update_time ON log.session_cache USING btree (update_time);


--
-- TOC entry 3548 (class 1259 OID 315731)
-- Name: mm_model_chat_log_send_user_id_idx; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_chat_log_send_user_id_idx ON log.mm_model_chat_log USING btree (send_user_id, send_time);


--
-- TOC entry 3549 (class 1259 OID 315732)
-- Name: mm_model_monitor_intf_idx1; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_intf_idx1 ON log.mm_model_monitor_intf USING btree (task_id);


--
-- TOC entry 3550 (class 1259 OID 315733)
-- Name: mm_model_monitor_intf_idx2; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_intf_idx2 ON log.mm_model_monitor_intf USING btree (intf_call_date_days);


--
-- TOC entry 3551 (class 1259 OID 315734)
-- Name: mm_model_monitor_intf_idx3; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_intf_idx3 ON log.mm_model_monitor_intf USING btree (intf_call_date);


--
-- TOC entry 3554 (class 1259 OID 315735)
-- Name: mm_model_monitor_model_idx1; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_model_idx1 ON log.mm_model_monitor_model USING btree (task_id);


--
-- TOC entry 3555 (class 1259 OID 315736)
-- Name: mm_model_monitor_model_idx2; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_model_idx2 ON log.mm_model_monitor_model USING btree (model_call_date_days);


--
-- TOC entry 3556 (class 1259 OID 315737)
-- Name: mm_model_monitor_model_idx3; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_model_idx3 ON log.mm_model_monitor_model USING btree (model_call_date);


--
-- TOC entry 3559 (class 1259 OID 315738)
-- Name: mm_model_monitor_statistics_idx1; Type: INDEX; Schema: log; Owner: -
--

CREATE INDEX mm_model_monitor_statistics_idx1 ON log.mm_model_monitor_statistics USING btree (task_id);


--
-- TOC entry 3580 (class 1259 OID 315739)
-- Name: mm_dict_data_dict_type_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_dict_data_dict_type_idx ON meta.mm_dict_data USING btree (dict_type);


--
-- TOC entry 3581 (class 1259 OID 315740)
-- Name: mm_dict_data_dict_value_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_dict_data_dict_value_idx ON meta.mm_dict_data USING btree (dict_value);


--
-- TOC entry 3582 (class 1259 OID 315741)
-- Name: mm_dict_data_dict_value_uidx; Type: INDEX; Schema: meta; Owner: -
--

CREATE UNIQUE INDEX mm_dict_data_dict_value_uidx ON meta.mm_dict_data USING btree (dict_value, dict_type);


--
-- TOC entry 3606 (class 1259 OID 315742)
-- Name: mm_order_node_order_code_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_order_node_order_code_idx ON meta.mm_order_node USING btree (order_code);


--
-- TOC entry 3607 (class 1259 OID 315743)
-- Name: mm_order_node_order_id_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_order_node_order_id_idx ON meta.mm_order_node USING btree (order_id);


--
-- TOC entry 3603 (class 1259 OID 315744)
-- Name: mm_order_order_code_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_order_order_code_idx ON meta.mm_order USING btree (order_code);


--
-- TOC entry 3610 (class 1259 OID 315745)
-- Name: mm_order_user_order_code_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_order_user_order_code_idx ON meta.mm_order_user USING btree (order_code);


--
-- TOC entry 3611 (class 1259 OID 315746)
-- Name: mm_order_user_order_id_idx; Type: INDEX; Schema: meta; Owner: -
--

CREATE INDEX mm_order_user_order_id_idx ON meta.mm_order_user USING btree (order_id);


--
-- TOC entry 3674 (class 1259 OID 327421)
-- Name: mm_train_dataset_join_task_id_idx; Type: INDEX; Schema: train; Owner: -
--

CREATE INDEX mm_train_dataset_join_task_id_idx ON train.mm_train_dataset_join USING btree (task_id, data_set_id);


-- Completed on 2025-08-04 15:32:31

--
-- PostgreSQL database dump complete
--

