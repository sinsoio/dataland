

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for co_access_record
-- ----------------------------
DROP TABLE IF EXISTS `co_access_record`;
CREATE TABLE `co_access_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `access_num` int(11) NOT NULL,
  `interface_name` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `account_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_account
-- ----------------------------
DROP TABLE IF EXISTS `co_account`;
CREATE TABLE `co_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `wallet_address` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `project_name` varchar(255) NOT NULL,
  `purpose` text NOT NULL,
  `api_key` varchar(255) NOT NULL,
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '1 Whitelist is available. 2 Blacklist is unavailable',
  PRIMARY KEY (`id`),
  UNIQUE KEY `address` (`wallet_address`) USING BTREE,
  UNIQUE KEY `apikey` (`api_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_dau
-- ----------------------------
DROP TABLE IF EXISTS `co_dau`;
CREATE TABLE `co_dau` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  `address` varchar(255) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userId_address_date` (`user_id`,`address`,`date`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_file_collection
-- ----------------------------
DROP TABLE IF EXISTS `co_file_collection`;
CREATE TABLE `co_file_collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `chain` varchar(255) NOT NULL COMMENT 'chain name',
  `nft_name` varchar(255) NOT NULL COMMENT 'NFT name',
  `nft_standerd` varchar(255) NOT NULL COMMENT 'standerd',
  `nft_contract` varchar(255) NOT NULL COMMENT 'nft contract',
  `nft_creater` varchar(255) NOT NULL COMMENT 'nft_creater',
  `nft_id` varchar(255) NOT NULL COMMENT 'nft_id',
  `nft_holder` varchar(255) NOT NULL COMMENT 'nft_holder',
  `minting_hash` varchar(255) DEFAULT NULL COMMENT 'minting hash',
  `minting_date` datetime NOT NULL COMMENT 'minting date',
  `nft_format` varchar(255) DEFAULT NULL COMMENT 'nft_format',
  `favorite_at` datetime NOT NULL COMMENT 'favorite_at',
  `source_url` varchar(255) NOT NULL COMMENT 'source url',
  `image_url` text COMMENT 'file url',
  `logo` text COMMENT 'logo',
  `sinso_url` varchar(255) DEFAULT NULL COMMENT 'sinso url',
  `upload_sinso_at` datetime DEFAULT NULL,
  `upload_sinso_state` int(1) NOT NULL COMMENT 'Upload sisnogetay status 1 to be uploaded 2 Upload succeeded 3 Upload failed 4 Do not upload without the source image',
  `upload_sinso_fail_at` datetime DEFAULT NULL COMMENT 'Failed to upload sinso. Next upload time',
  `upload_sinso_fail_msg` text,
  `is_collected` tinyint(1) DEFAULT NULL COMMENT 'Is it your own collection?',
  `is_created` tinyint(1) DEFAULT NULL COMMENT 'Whether you created it yourself',
  `is_favorited` tinyint(1) DEFAULT NULL COMMENT 'Are you paying attention to yourself',
  `is_manually_click` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether to manually click Upload',
  `is_del` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Delete Status 0 Normal 1 Deleted',
  `is_mint` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'Whether it was cast in dataland',
  `del_at` datetime DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `folder_id` int(11) NOT NULL,
  `attributes` text,
  `token_uri` varchar(255) DEFAULT NULL COMMENT 'tokneUri',
  `chain_get_state` int(1) NOT NULL DEFAULT '1' COMMENT 'On-chain data acquisition status 1 is not obtained, 2 is obtained successfully and 3 is obtained failed',
  `chain_get_state_fail_msg` text COMMENT 'The cause of the failure to obtain data on the chain',
  `chain_get_state_fail_at` datetime DEFAULT NULL COMMENT 'On-chain data acquisition failure time',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1217 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_folder
-- ----------------------------
DROP TABLE IF EXISTS `co_folder`;
CREATE TABLE `co_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `folder_name` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  `parent_id` int(11) NOT NULL DEFAULT '0',
  `bread_crumbs` text,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=865 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_nft_json
-- ----------------------------
DROP TABLE IF EXISTS `co_nft_json`;
CREATE TABLE `co_nft_json` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `json_str` text NOT NULL,
  `cid` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) NOT NULL,
  `upload_address` int(2) NOT NULL,
  `nft_contract` varchar(255) DEFAULT NULL,
  `nft_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_record
-- ----------------------------
DROP TABLE IF EXISTS `co_record`;
CREATE TABLE `co_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL,
  `tx_from_address` varchar(255) NOT NULL COMMENT 'from',
  `tx_to_address` varchar(255) NOT NULL COMMENT 'to',
  `tx_send_address` varchar(255) NOT NULL,
  `tx_receive_address` varchar(255) NOT NULL,
  `tx_token_id` varchar(255) NOT NULL,
  `tx_time` datetime NOT NULL,
  `block_number` varchar(255) NOT NULL,
  `block_hash` varchar(255) NOT NULL,
  `tx_hash` varchar(255) NOT NULL,
  `chain` varchar(255) NOT NULL,
  `type` int(1) NOT NULL COMMENT 'Received by 1 and sent by 2',
  `state` tinyint(1) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=741 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_scan_config
-- ----------------------------
DROP TABLE IF EXISTS `co_scan_config`;
CREATE TABLE `co_scan_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `api_key` varchar(128) NOT NULL,
  `key_lock_at` datetime DEFAULT NULL COMMENT 'Lock time - The number of calls used up the time available for the next call',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for co_user
-- ----------------------------
DROP TABLE IF EXISTS `co_user`;
CREATE TABLE `co_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `revision` int(11) NOT NULL DEFAULT '1',
  `registered_at` datetime NOT NULL,
  `address` varchar(128) NOT NULL,
  `chain` varchar(128) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `message_end_at` datetime DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL COMMENT 'token',
  `token_end_at` datetime DEFAULT NULL,
  `type` int(1) NOT NULL COMMENT 'Initialization status 1 Uninitialized 2 Initializing 3 Initializing completed',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
