SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `bungeecord_proxy_manager` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `bungeecord_proxy_manager`;

CREATE TABLE `banned_players` (
  `id` int(10) UNSIGNED NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `uuid` varchar(36) COLLATE utf8_bin NOT NULL,
  `username` text COLLATE utf8_bin NOT NULL COMMENT 'Only used to identify players',
  `message` text COLLATE utf8_bin NOT NULL,
  `expires` datetime DEFAULT NULL,
  `banned_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `blocked_domains` (
  `id` int(11) NOT NULL,
  `domain` text COLLATE utf8_bin NOT NULL,
  `message` text COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `name_history` (
  `id` int(10) UNSIGNED NOT NULL,
  `uuid` varchar(36) COLLATE utf8_bin NOT NULL,
  `change_detected` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `old_name` text COLLATE utf8_bin NOT NULL,
  `new_name` text COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `players` (
  `id` int(10) UNSIGNED NOT NULL,
  `uuid` varchar(36) COLLATE utf8_bin NOT NULL,
  `username` text COLLATE utf8_bin NOT NULL,
  `first_join_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_join_timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_join_ip` text COLLATE utf8_bin NOT NULL,
  `last_join_hostname` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `servers` (
  `id` int(11) NOT NULL,
  `host` varchar(255) COLLATE utf8_bin NOT NULL,
  `port` smallint(5) UNSIGNED NOT NULL DEFAULT '25565',
  `prefix` text COLLATE utf8_bin,
  `name` varchar(128) COLLATE utf8_bin NOT NULL,
  `displayname` text COLLATE utf8_bin NOT NULL,
  `lore` text COLLATE utf8_bin NOT NULL,
  `use_domain` tinyint(1) NOT NULL DEFAULT '0',
  `domain` varchar(512) COLLATE utf8_bin DEFAULT NULL,
  `require_direct_join` tinyint(1) NOT NULL DEFAULT '0',
  `show_in_server_list` tinyint(1) NOT NULL DEFAULT '1',
  `icon` text COLLATE utf8_bin
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


ALTER TABLE `banned_players`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `blocked_domains`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `name_history`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `players`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uuid` (`uuid`);

ALTER TABLE `servers`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `domain` (`domain`);


ALTER TABLE `banned_players`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `blocked_domains`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
ALTER TABLE `name_history`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
ALTER TABLE `players`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
ALTER TABLE `servers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
