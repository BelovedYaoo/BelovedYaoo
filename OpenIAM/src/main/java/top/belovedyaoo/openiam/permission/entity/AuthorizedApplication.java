package top.belovedyaoo.openiam.permission.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.annotation.mysql.MysqlTypeConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import top.belovedyaoo.agcore.base.BaseFiled;
import top.belovedyaoo.openiam.oauth2.data.model.loader.OpenAuthClientModel;
import top.belovedyaoo.openiam.permission.toolkit.ListStringTypeHandler;

import java.io.Serializable;
import java.util.List;

/**
 * (AuthAPP)表持久化对象<br>
 * 用于存储授权应用信息
 *
 * @author BelovedYaoo
 * @version 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@Getter(onMethod_ = @JsonGetter)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true, fluent = true)
@Table(value = "auth_app", dataSource = "primary")
public class AuthorizedApplication extends BaseFiled implements Serializable {

    /**
     * 应用名称
     */
    @ColumnNotNull
    @ColumnComment("应用名称")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 15)
    private String clientName;

    /**
     * 应用ID，该ID为鉴别应用身份使用
     */
    @ColumnNotNull
    @ColumnComment("应用ID")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 32)
    private String clientId;

    /**
     * 应用密钥
     */
    @ColumnNotNull
    @ColumnComment("应用密钥")
    @ColumnType(value = MysqlTypeConstant.VARCHAR, length = 255)
    private String clientSecret;

    /**
     * 权限范围
     */
    @ColumnComment("签约的所有权限")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    @Column(typeHandler = ListStringTypeHandler.class)
    private List<String> contractScopes;

    /**
     * 授权链接
     */
    @ColumnNotNull
    @ColumnComment("允许授权的redirect_uri")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    @Column(typeHandler = ListStringTypeHandler.class)
    private List<String> allowRedirectUris;

    /**
     * 授权类型
     */
    @ColumnNotNull
    @ColumnComment("允许的授权类型")
    @ColumnType(value = MysqlTypeConstant.TEXT)
    @Column(typeHandler = ListStringTypeHandler.class)
    private List<String> allowGrantTypes;

    /**
     * 是否同时刷新 Refresh-Token
     */
    @ColumnComment("单独配置此应用：是否在每次 Refresh-Token 刷新 Access-Token 时，产生一个新的 Refresh-Token [默认取全局配置]")
    @ColumnType(value = MysqlTypeConstant.TINYINT, length = 1)
    private Boolean isNewRefresh;

    /**
     * Access-Token 过期时间
     */
    @ColumnComment("单独配置此应用：Access-Token 保存的时间(单位秒) [默认取全局配置]")
    @ColumnType(value = MysqlTypeConstant.BIGINT)
    private Long accessTokenTimeout;

    /**
     * Refresh-Token 过期时间
     */
    @ColumnComment("单独配置此应用：Refresh-Token 保存的时间(单位秒) [默认取全局配置]")
    @ColumnType(value = MysqlTypeConstant.BIGINT)
    private Long refreshTokenTimeout;

    /**
     * Client-Token 过期时间
     */
    @ColumnComment("单独配置此应用：Client-Token 保存的时间(单位秒) [默认取全局配置]")
    @ColumnType(value = MysqlTypeConstant.BIGINT)
    private Long clientTokenTimeout;

    /**
     * Lower-Client-Token 过期时间
     */
    @ColumnComment("单独配置此应用：Lower-Client-Token 保存的时间(单位：秒) [默认取全局配置]")
    @ColumnType(value = MysqlTypeConstant.BIGINT)
    private Long lowerClientTokenTimeout;

    public OpenAuthClientModel convertToClientModel() {
        return new OpenAuthClientModel()
                .setClientId(this.clientId())
                .setClientSecret(this.clientSecret())
                .addAllowRedirectUris(this.allowRedirectUris().toArray(new String[0]))
                .addContractScopes(this.contractScopes().toArray(new String[0]))
                .addAllowGrantTypes(this.allowGrantTypes().toArray(new String[0]));
                // .setIsNewRefresh(this.isNewRefresh())
                // .setAccessTokenTimeout(this.accessTokenTimeout())
                // .setRefreshTokenTimeout(this.refreshTokenTimeout())
                // .setClientTokenTimeout(this.clientTokenTimeout())
                // .setLowerClientTokenTimeout(this.lowerClientTokenTimeout());
    }

}
