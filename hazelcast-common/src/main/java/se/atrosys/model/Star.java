package se.atrosys.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TODO write documentation
 */
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gaia_source")
public class Star implements Serializable {
	@Id
	private Long sourceId;
	@Column
	private Long solutionId;
	@Column
	private Long randomIndex;
	@Column
	private Double refEpoch;
	@Column
	private Double ra;
	@Column
	private Double raError;
	@Column
	private Double dec;
	@Column
	private Double decError;
	@Column
	private Double parallax;
	@Column
	private Double parallaxError;
	@Column
	private Double pmra;
	@Column
	private Double pmraError;
	@Column
	private Double pmdec;
	@Column
	private Double pmdecError;
	@Column
	private Double raDecCorr;
	@Column
	private Double raParallaxCorr;
	@Column
	private Double raPmraCorr;
	@Column
	private Double raPmdecCorr;
	@Column
	private Double decParallaxCorr;
	@Column
	private Double decPmraCorr;
	@Column
	private Double decPmdecCorr;
	@Column
	private Double parallaxPmraCorr;
	@Column
	private Double parallaxPmdecCorr;
	@Column
	private Double pmraPmdecCorr;
	@Column(name = "astrometric_n_obs_al")
	private Integer astrometricNObsAl;
	@Column(name = "astrometric_n_obs_ac")
	private Integer astrometricNObsAc;
	@Column(name = "astrometric_n_good_obs_al")
	private Integer astrometricNGoodObsAl;
	@Column(name = "astrometric_n_good_obs_ac")
	private Integer astrometricNGoodObsAc;
	@Column(name = "astrometric_n_bad_obs_al")
	private Integer astrometricNBadObsAl;
	@Column(name = "astrometric_n_bad_obs_ac")
	private Integer astrometricNBadObsAc;
	@Column(name = "astrometric_delta_q")
	private Double astrometricDeltaQ;
	@Column
	private Double astrometricExcessNoise;
	@Column
	private Double astrometricExcessNoiseSig;
	@Column
	private Boolean astrometricPrimaryFlag;
	@Column
	private Double astrometricRelegationFactor;
	@Column
	private Double astrometricWeightAl;
	@Column
	private Double astrometricWeightAc;
	@Column
	private Integer astrometricPriorsUsed;
	@Column
	private Integer matchedObservations;
	@Column
	private Boolean duplicatedSource;
	@Column(name = "scan_direction_strength_k1")
	private Double scanDirectionStrengthK1;
	@Column(name = "scan_direction_strength_k2")
	private Double scanDirectionStrengthK2;
	@Column(name = "scan_direction_strength_k3")
	private Double scanDirectionStrengthK3;
	@Column(name = "scan_direction_strength_k4")
	private Double scanDirectionStrengthK4;
	@Column(name = "scan_direction_mean_k1")
	private Double scanDirectionMeanK1;
	@Column(name = "scan_direction_mean_k2")
	private Double scanDirectionMeanK2;
	@Column(name = "scan_direction_mean_k3")
	private Double scanDirectionMeanK3;
	@Column(name = "scan_direction_mean_k4")
	private Double scanDirectionMeanK4;
	@Column(name = "phot_g_n_obs")
	private Integer photGNObs;
	@Column(name = "phot_g_mean_flux")
	private Double photGMeanFlux;
	@Column(name = "phot_g_mean_flux_error")
	private Double photGMeanFluxError;
	@Column(name = "phot_g_mean_mag")
	private Double photGMeanMag;
	@Column
	private String photVariableFlag;
	@Column
	private Double l;
	@Column
	private Double b;
	@Column
	private Double eclLon;
	@Column
	private Double eclLat;
}
