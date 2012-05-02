package alice.sapere.controller.networking;

import java.io.Serializable;

/**
 * Class that represent a single operation requested to the System
 * 
 * @author Marco Santarelli
 * 
 */
public class SapereOperation implements Serializable {

	private static final long serialVersionUID = -4208008724529365093L;
	private Lsa lsa;
	private LsaId lsaId;
	private SapereOperationType type;
	private Content content;
	private IContentFilter filter;
	private String requestingId;

	public SapereOperation(final SapereOperationType aType, final LsaId anLsaId,
			final Lsa aSubject, final Content newCon, final String aRequestingId,
			final IContentFilter aFilter) {
		type = aType;
		lsaId = anLsaId;
		lsa = aSubject;
		content = newCon;
		requestingId = aRequestingId;
		filter = aFilter;
	}

	public final String getRequestingId() {
		return requestingId;
	}

	public final void setRequestingId(final String agentId) {
		this.requestingId = agentId;
	}

	public final Lsa getLsa() {
		return lsa;
	}

	public final void setLsa(final Lsa anLsa) {
		lsa = anLsa;
	}

	public final LsaId getLsaId() {
		return lsaId;
	}

	public final void setLsaId(final LsaId id) {
		lsaId = id;
	}

	public final SapereOperationType getType() {
		return type;
	}

	public final void setType(final SapereOperationType aType) {
		type = aType;
	}

	public final Content getNewContent() {
		return content;
	}

	public final void setNewContent(final Content con) {
		content = con;
	}

	public final IContentFilter getFilter() {
		return filter;
	}

	public final void setFilter(final IContentFilter aFilter) {
		filter = aFilter;
	}

	public final SapereOperation getCopy() {
		return new SapereOperation(type, lsaId, lsa.getCopy(),
				content.getCopy(), requestingId, filter);
	}

	@Override
	public final String toString() {
		String res = "operation[type=" + type;
		if (lsaId != null) {
			res += ",lsaId=" + lsaId;
		}
		if (lsa != null) {
			res += ",lsa=" + lsa;
		}
		if (content != null) {
			res += ",newContent=" + content;
		}
		if (filter != null) {
			res += ",filter=" + filter;
		}
		if (requestingId != null) {
			res += ",requestedBy=" + requestingId;
		}
		return res += "]";
	}

}
