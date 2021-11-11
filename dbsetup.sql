--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 14.0

-- Started on 2021-11-11 15:39:56 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 980205)
-- Name: test_table; Type: TABLE; Schema: public; Owner: test_user
--

CREATE TABLE public.test_table (
    test_id bigint NOT NULL
);


ALTER TABLE public.test_table OWNER TO test_user;

--
-- TOC entry 3245 (class 0 OID 980205)
-- Dependencies: 200
-- Data for Name: test_table; Type: TABLE DATA; Schema: public; Owner: test_user
--

COPY public.test_table (test_id) FROM stdin;
42
\.


--
-- TOC entry 3114 (class 2606 OID 980209)
-- Name: test_table test_table_pkey; Type: CONSTRAINT; Schema: public; Owner: test_user
--

ALTER TABLE ONLY public.test_table
    ADD CONSTRAINT test_table_pkey PRIMARY KEY (test_id);


-- Completed on 2021-11-11 15:39:56 CET

--
-- PostgreSQL database dump complete
--

